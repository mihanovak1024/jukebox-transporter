package jukebox.youtube;

import jukebox.BaseTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class YoutubeSearchResponseParserTest extends BaseTest {

    private YoutubeSearchResponseParser youtubeSearchResponseParser;

    @Before
    public void setUp() {
        youtubeSearchResponseParser = new YoutubeSearchResponseParser();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createYoutubeSearchDataFromResponse_invalidHtmlResponse_youtubeParserExceptionThrown() throws IOException {
        // given
        expectedException.expect(YoutubeParserException.class);
        expectedException.expectMessage(YoutubeParserException.SEARCH_JSON_RESPONSE_PARSE_ERROR);

        String responseHtml = "<html></html>";
        YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo
                .YoutubeSearchInfoBuilder()
                .song("randomSong")
                .artist("randomArtist")
                .previousLinks(null)
                .build();

        // when
        youtubeSearchResponseParser.createYoutubeSearchDataFromResponse(responseHtml, youtubeSearchInfo);

        // then
    }

    @Test
    public void createYoutubeSearchDataFromResponse_invalidHtmlResponseWithNoNewline_youtubeParserExceptionThrown() throws IOException {
        // given
        expectedException.expect(YoutubeParserException.class);
        expectedException.expectMessage(YoutubeParserException.SEARCH_JSON_RESPONSE_PARSE_ERROR);

        String responseHtml = "<html>" +
                "[\"ytInitialData\"] = {\"responseContext\": lala};window[\"ytInitialPlayerResponse\"] = null;" +
                "</html>";
        YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo
                .YoutubeSearchInfoBuilder()
                .song("randomSong")
                .artist("randomArtist")
                .previousLinks(null)
                .build();

        // when
        youtubeSearchResponseParser.createYoutubeSearchDataFromResponse(responseHtml, youtubeSearchInfo);

        // then
    }

    @Test
    public void createYoutubeSearchDataFromResponse_invalidHtmlResponse_jacksonParsingExceptionThrown() throws IOException {
        // given
        expectedException.expect(IOException.class);

        String responseHtml = "<html>" +
                "[\"ytInitialData\"] = {\"responseContext\": lala};\n" +
                "window[\"ytInitialPlayerResponse\"] = null;" +
                "</html>";
        YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo
                .YoutubeSearchInfoBuilder()
                .song("randomSong")
                .artist("randomArtist")
                .previousLinks(null)
                .build();

        // when
        youtubeSearchResponseParser.createYoutubeSearchDataFromResponse(responseHtml, youtubeSearchInfo);

        // then
    }

    @Test
    public void createYoutubeSearchDataFromResponse_invalidHtmlResponseIncompleteTree_youtubeParserExceptionThrown() throws IOException {
        // given
        expectedException.expect(YoutubeParserException.class);
        expectedException.expectMessage(YoutubeParserException.SEARCH_DATA_PARSE_ERROR);

        String responseHtml = "<html>" +
                "[\"ytInitialData\"] = {\"responseContext\": {" +
                "\"contents\": {" +
                "\"twoColumnSearchResultsRenderer\":{}" +
                "}" +
                "}" +
                "};\n" +
                "window[\"ytInitialPlayerResponse\"] = null;" +
                "</html>";
        YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo
                .YoutubeSearchInfoBuilder()
                .song("randomSong")
                .artist("randomArtist")
                .previousLinks(null)
                .build();

        // when
        youtubeSearchResponseParser.createYoutubeSearchDataFromResponse(responseHtml, youtubeSearchInfo);

        // then
    }

    @Test
    public void createYoutubeSearchDataFromResponse_validHtmlResponse_validSongAndLink() throws IOException {
        // given
        String responseHtml = HtmlResultConstants.VALID_HTML_RESPONSE_ONE_VIDEO_RENDERER;
        YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo
                .YoutubeSearchInfoBuilder()
                .song("randomSong")
                .artist("randomArtist")
                .build();

        // when
        YoutubeSearchData youtubeSearchData = youtubeSearchResponseParser.createYoutubeSearchDataFromResponse(responseHtml, youtubeSearchInfo);

        // then
        assertThat(youtubeSearchData.getTitle(), equalTo(HtmlResultConstants.VALID_HTML_RESPONSE_FIRST_SONG_NAME));
        assertThat(youtubeSearchData.getYoutubeLink(), equalTo(String.format(YoutubeConstants.VIDEO_URL_FORMAT, HtmlResultConstants.VALID_HTML_RESPONSE_FIRST_SONG_VIDEO_ID)));
    }

    @Test
    public void createYoutubeSearchDataFromResponse_validHtmlResponseWithPreviousLinks_validSecondSongAndLink() throws IOException {
        // given
        String responseHtml = HtmlResultConstants.VALID_HTML_RESPONSE_TWO_VIDEO_RENDERERS;
        List<String> previousLinks = new ArrayList<>();
        previousLinks.add(String.format(YoutubeConstants.VIDEO_URL_FORMAT, HtmlResultConstants.VALID_HTML_RESPONSE_FIRST_SONG_VIDEO_ID));
        YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo
                .YoutubeSearchInfoBuilder()
                .song("randomSong")
                .artist("randomArtist")
                .previousLinks(previousLinks)
                .build();

        // when
        YoutubeSearchData youtubeSearchData = youtubeSearchResponseParser.createYoutubeSearchDataFromResponse(responseHtml, youtubeSearchInfo);

        // then
        assertThat(youtubeSearchData.getTitle(), equalTo(HtmlResultConstants.VALID_HTML_RESPONSE_SECOND_SONG_NAME));
        assertThat(youtubeSearchData.getYoutubeLink(), equalTo(String.format(YoutubeConstants.VIDEO_URL_FORMAT, HtmlResultConstants.VALID_HTML_RESPONSE_SECOND_SONG_VIDEO_ID)));
    }

    @Test
    public void createYoutubeSearchDataFromResponse_validHtmlResponseWithPreviousLinks_noThirdOptionThrowException() throws IOException {
        // given
        expectedException.expect(YoutubeParserException.class);
        expectedException.expectMessage(YoutubeParserException.SEARCH_DATA_VIDEO_RENDERER_PARSE_ERROR);

        String responseHtml = HtmlResultConstants.VALID_HTML_RESPONSE_TWO_VIDEO_RENDERERS;
        List<String> previousLinks = new ArrayList<>();
        previousLinks.add(String.format(YoutubeConstants.VIDEO_URL_FORMAT, HtmlResultConstants.VALID_HTML_RESPONSE_FIRST_SONG_VIDEO_ID));
        previousLinks.add(String.format(YoutubeConstants.VIDEO_URL_FORMAT, HtmlResultConstants.VALID_HTML_RESPONSE_SECOND_SONG_VIDEO_ID));
        YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo
                .YoutubeSearchInfoBuilder()
                .song("randomSong")
                .artist("randomArtist")
                .previousLinks(previousLinks)
                .build();

        // when
        youtubeSearchResponseParser.createYoutubeSearchDataFromResponse(responseHtml, youtubeSearchInfo);

        // then
    }
}