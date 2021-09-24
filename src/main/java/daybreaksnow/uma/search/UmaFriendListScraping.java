package daybreaksnow.uma.search;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

/**
 * GameWithのフレンド掲示板から条件に合致するリストを検索する
 * @author daybreaksnow
 *
 */
public class UmaFriendListScraping {
	private static final String TOP_URL = "https://gamewith.jp/uma-musume/article/show/260740";

	private static final String SHADOW_ROOT = "gds-uma-musume-friends";

	public UmaFriendListScraping() {
		Configuration.browser = WebDriverRunner.CHROME;
		Configuration.headless = true;
	}

	@SuppressWarnings("deprecation")
	public String scraping(String representativeUmaName, Collection<String> factors, int nextNum) {
		try {
			open(TOP_URL);
			// NOTE 広告を消しておかないとボタンクリック時にelement click interceptedでこける
			SelenideElement adCloseButton = $("#gCloseButton");
			adCloseButton.click();
			// 詳細条件を指定して検索
			// NOTE 原因不明だが、たまにこの要素が取得できなくてこける。sleepすると直るので入れているが、waitUntilに出来ればそうすべき
			// Other element would receive the click: <a href="https://gamewith.jp/uma-musume/article/show/258347">...</a>
			Thread.sleep(100);
			SelenideElement openDialogButton = $(shadowCss(".-r-uma-musume-friends-button.secondary", SHADOW_ROOT));
			openDialogButton.click();
			// 代表ウマ娘
			// NOTE 原因不明だがたまにこの要素が取得できなくてこける。sleepすると直るので入れているが、waitUntilに出来ればそうすべき
			Thread.sleep(100);
			SelenideElement umaCombo = $(
					shadowCss(".-r-uma-musume-friends-search-modal__umamusume-select", SHADOW_ROOT));
			if (representativeUmaName != null) {
				umaCombo.selectOptionContainingText(representativeUmaName);
			}
			// NOTE 原因不明だが稀にこの要素が取得できなくてこける。sleepすると直るので入れているが、waitUntilに出来ればそうすべき
			Thread.sleep(100);
			// NOTE 赤因子、青因子の両方とも同じスタイルなので全部取れる
			ElementsCollection checkBoxes = $$(shadowCss(".-r-uma-musume-friends-search-modal__label", SHADOW_ROOT));
			for (SelenideElement checkBox : checkBoxes) {
				String text = checkBox.getText();
				if (factors.contains(text)) {
					checkBox.click();
				}
			}
			// 検索する
			// NOTE このclassは複数存在しており一意に特定できないため、一回検索してからtextを見て判断
			ElementsCollection buttons = $$(shadowCss(".-r-uma-musume-friends-button.primary", SHADOW_ROOT));
			buttons.stream().filter(e -> e.getText().equals("検索する")).findFirst().ifPresent(e -> e.click());
			// 「検索中です」が消えるまで待つ
			SelenideElement loading = $(
					shadowCss(".-r-uma-musume-friends__loading", SHADOW_ROOT));
			loading.waitUntil(Condition.hidden, Configuration.timeout);

			// 「もっと見る」を指定回数押す
			for (int i = 0; i < nextNum; i++) {
				SelenideElement nextButton = $(
						shadowCss(".-r-uma-musume-friends-button.primary.-r-uma-musume-friends__next", SHADOW_ROOT));
				if (nextButton.exists()) {
					// NOTE clickだと「Other element would receive the click: <a href="https://gamewith.jp/uma-musume/article/show/258347">...</a>」と言われてこける
					nextButton.pressEnter();
					// 「検索中です」が消えるまで待つ
					loading.waitUntil(Condition.hidden, Configuration.timeout);
				} else {
					break;
				}
			}

			// 各情報を取得
			SelenideElement friendListRootElement = $(shadowCss(".-r-uma-musume-friends__list-wrap", SHADOW_ROOT));
			// 条件を変えて繰り返し抽出しやすいよう、必要な情報をHTMLとして返す
			return friendListRootElement.innerHtml();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		UmaFriendListScraping searcher = new UmaFriendListScraping();
		String friendHtml = searcher.scraping(
				"テイオー",
				Arrays.asList("中距離"),
				2);
		System.out.println(friendHtml);
		Path dest = Paths.get("friend.txt");
		Files.write(dest, Arrays.asList(friendHtml));
	}
}
