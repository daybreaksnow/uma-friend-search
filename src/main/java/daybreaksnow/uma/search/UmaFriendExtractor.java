package daybreaksnow.uma.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * GameWithのフレンド掲示板の情報から検索条件に合致するものを抽出する
 * @author daybreaksnow
 *
 */
public class UmaFriendExtractor {
	/**
	 * @param friendHtml 解析対象のHTML
	 * @param representNeedFactors 代表ウマ娘に必要な因子
	 * @param allNeedFactors 代表とその親合計で必要な因子
	 * @param representExcludeFactors 代表ウマ娘が持っていてはいけない因子
	 * @param allExcludeFactors 代表とその親のいずれも持っていてはいけない因子
	 */
	public Collection<FriendInfo> extract(String friendHtml,
			Collection<Factor> representNeedFactors,
			Collection<Factor> allNeedFactors,
			Collection<String> representExcludeFactors,
			Collection<String> allExcludeFactors) {
		Document doc = Jsoup.parse(friendHtml);
		Elements friendElements = doc.select(".-r-uma-musume-friends-list-item__contents");
		Collection<FriendInfo> friendInfos = new ArrayList<>();
		for (Element element : friendElements) {
			Element trainerElement = element.selectFirst(".-r-uma-musume-friends-list-item__trainerId__text");
			String trainerId = trainerElement.text();

			boolean isValid = true;
			Collection<String> representFactors = new ArrayList<>();
			Collection<String> allFactors = new ArrayList<>();
			Set<Factor> allHitFactors = new HashSet<Factor>();
			Set<Factor> representHitFactors = new HashSet<Factor>();

			Elements factors = element.selectFirst(".-r-uma-musume-friends-list-item__factor-list").children();
			for (Element factorElement : factors) {
				Element valueElem = factorElement.child(0);
				String factorStr = valueElem.text();
				// 持っていてはいけない因子があったらそこで終わり
				for (String excludeWord : allExcludeFactors) {
					if (factorStr.contains(excludeWord)) {
						isValid = false;
						break;
					}
				}
				for (String excludeWord : representExcludeFactors) {
					if (factorStr.contains(excludeWord) && factorStr.contains("代表")) {
						isValid = false;
						break;
					}
				}
				if (!isValid) {
					break;
				}
				// 探したい因子があるか
				// XXX すでにヒットした因子は除外したい
				for (Factor factor : allNeedFactors) {
					String pattern = String.format(".*%s.*[%d-9].*", factor.getName(), factor.getMinNum());
					if (factorStr.matches(pattern)) {
						allHitFactors.add(factor);
						break;
					}
				}
				for (Factor factor : representNeedFactors) {
					String pattern = String.format(".*%s.*代表[%d-3].*", factor.getName(), factor.getMinNum());
					if (factorStr.matches(pattern)) {
						representHitFactors.add(factor);
						break;
					}
				}
				if (factorStr.contains("代表")) {
					// TODO ここの文字列は「スタミナ9(代表3)」のように不要な情報が含まれているので、「スタミナ3」のようにしたい
					representFactors.add(factorStr);
				}
				allFactors.add(factorStr);
			}

			if (isValid && allNeedFactors.size() == allHitFactors.size()
					&& representNeedFactors.size() == representHitFactors.size()) {
				friendInfos.add(new FriendInfo(trainerId, allFactors, representFactors));
			}
		}
		//検索結果数も出しておく
		// TODO debugレベルで出したい
		//		Element countElement = doc.selectFirst(".-r-uma-musume-friends__results");
		//		System.out.println(countElement.text());
		//		System.out.println("条件に合致する件数：" + friendInfos.size());
		return friendInfos;
	}

}
