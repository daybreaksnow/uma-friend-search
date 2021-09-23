package daybreaksnow.uma.search;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import org.junit.Test;

/**
 * 
 * @author daybreaksnow
 */
public class UmaFriendExtractorTest {
	private UmaFriendExtractor extractor = new UmaFriendExtractor();

	@Test
	public void testExtract_条件なし() {
		String html = getHtml("friend.txt");
		Collection<Factor> representNeedFactors = Collections.emptyList();
		Collection<Factor> allNeedFactors = Collections.emptyList();
		Collection<String> representExcludeFactors = Collections.emptyList();
		Collection<String> allExcludeFactors = Collections.emptyList();
		Collection<FriendInfo> result = extractor.extract(html, representNeedFactors, allNeedFactors,
				representExcludeFactors, allExcludeFactors);
		assertThat(result.size(), is(3));
	}

	@Test
	public void textExtract_代表条件あり() {
		String html = getHtml("friend.txt");
		Collection<Factor> representNeedFactors = Arrays.asList(new Factor("スタミナ", 3), new Factor("長距離", 1));
		Collection<Factor> allNeedFactors = Collections.emptyList();
		Collection<String> representExcludeFactors = Collections.emptyList();
		Collection<String> allExcludeFactors = Collections.emptyList();
		Collection<FriendInfo> result = extractor.extract(html, representNeedFactors, allNeedFactors,
				representExcludeFactors, allExcludeFactors);
		assertThat(result.size(), is(1));
		assertThat(result.iterator().next().getTrainderId(), is("trainerId3"));
	}

	@Test
	public void textExtract_全体条件あり() {
		String html = getHtml("friend.txt");
		Collection<Factor> representNeedFactors = Collections.emptyList();
		Collection<Factor> allNeedFactors = Arrays.asList(new Factor("スタミナ", 9), new Factor("長距離", 3),
				new Factor("芝", 1));
		Collection<String> representExcludeFactors = Collections.emptyList();
		Collection<String> allExcludeFactors = Collections.emptyList();
		Collection<FriendInfo> result = extractor.extract(html, representNeedFactors, allNeedFactors,
				representExcludeFactors, allExcludeFactors);
		assertThat(result.size(), is(1));
		assertThat(result.iterator().next().getTrainderId(), is("trainerId1"));
	}

	@Test
	public void textExtract_代表除外条件あり() {
		String html = getHtml("friend.txt");
		Collection<Factor> representNeedFactors = Collections.emptyList();
		Collection<Factor> allNeedFactors = Collections.emptyList();
		Collection<String> representExcludeFactors = Arrays.asList("中距離");
		Collection<String> allExcludeFactors = Collections.emptyList();
		Collection<FriendInfo> result = extractor.extract(html, representNeedFactors, allNeedFactors,
				representExcludeFactors, allExcludeFactors);
		assertThat(result.size(), is(2));
	}

	@Test
	public void textExtract_全体除外条件あり() {
		String html = getHtml("friend.txt");
		Collection<Factor> representNeedFactors = Collections.emptyList();
		Collection<Factor> allNeedFactors = Collections.emptyList();
		Collection<String> representExcludeFactors = Collections.emptyList();
		Collection<String> allExcludeFactors = Arrays.asList("先行");
		Collection<FriendInfo> result = extractor.extract(html, representNeedFactors, allNeedFactors,
				representExcludeFactors, allExcludeFactors);
		assertThat(result.size(), is(2));
	}

	private String getPath(String fileName) {
		URL url = this.getClass().getResource(fileName);
		String filePath;
		try {
			filePath = URLDecoder.decode(url.getFile(), "UTF-8").replaceFirst("^/(.:/)", "$1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return filePath;
	}

	private String getHtml(String fileName) {
		String path = getPath(fileName);
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		StringJoiner joiner = new StringJoiner("");
		lines.stream().forEach(joiner::add);
		return joiner.toString();
	}
}
