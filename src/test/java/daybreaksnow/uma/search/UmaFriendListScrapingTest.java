package daybreaksnow.uma.search;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

/**
 * 
 * @author daybreaksnow
 */
public class UmaFriendListScrapingTest {
	private UmaFriendListScraping scraping = new UmaFriendListScraping();

	@Test
	public void testScraping_条件なし() throws InterruptedException {
		String resultHtml = scraping.scraping(null, Collections.emptyList(), Collections.emptyList(), 0);
		assertThat(resultHtml.contains("class=\"-r-uma-musume-friends__list\""), is(true));
		assertThat(resultHtml.contains("class=\"-r-uma-musume-friends__results\""), is(true));
		assertThat(resultHtml.contains("直近200件"), is(true));
	}

	@Test
	public void testScraping_もっと見る回数() throws InterruptedException {
		String resultHtml = scraping.scraping(null, Collections.emptyList(), Collections.emptyList(), 2);
		assertThat(resultHtml.contains("class=\"-r-uma-musume-friends__list\""), is(true));
		assertThat(resultHtml.contains("class=\"-r-uma-musume-friends__results\""), is(true));
		assertThat(resultHtml.contains("直近600件"), is(true));
	}

	@Test
	public void testScraping_条件あり() throws InterruptedException {
		String resultHtml = scraping.scraping("ライスシャワー", Arrays.asList("スタミナ"), Arrays.asList("長距離"), 0);
		assertThat(resultHtml.contains("class=\"-r-uma-musume-friends__list\""), is(true));
		assertThat(resultHtml.contains("class=\"-r-uma-musume-friends__results\""), is(true));
		assertThat(resultHtml.contains("直近200件"), is(true));
		// NOTE この条件に合致しないものがヒットしていないかをチェックするべきだが、そこまで厳密にはやらない
	}
}
