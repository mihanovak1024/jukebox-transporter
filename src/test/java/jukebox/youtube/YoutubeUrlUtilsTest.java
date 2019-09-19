package jukebox.youtube;

import jukebox.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class YoutubeUrlUtilsTest extends BaseTest {

    @Test
    public void getVideoIdFromUrl_baseUrl_validData() {
        // given
        String videoId = "e15115afTT3ta";
        String url = String.format("watch?v=%s", videoId);

        // when
        String wantedVideoId = YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test(expected = YoutubeParserException.class)
    public void getVideoIdFromUrl_nullUrl_youtubeParseError() {
        // given
        String url = null;

        // when
        YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        // Exception
    }

    @Test
    public void getVideoIdFromUrl_urlWithUnderscore_validData() {
        // given
        String videoId = "e15115_afTT_3ta";
        String url = String.format("watch?v=%s", videoId);

        // when
        String wantedVideoId = YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromUrl_urlWithDash_validData() {
        // given
        String videoId = "e15115-afTT-3ta";
        String url = String.format("watch?v=%s", videoId);

        // when
        String wantedVideoId = YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromUrl_urlWithSlashes_validData() {
        // given
        String videoId = "feafaefaef3145251";
        String url = String.format("watch?v=%s/afefa4", videoId);

        // when
        String wantedVideoId = YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromUrl_urlWithQueryParams_validData() {
        // given
        String videoId = "feafaefaef3145251";
        String url = String.format("watch?v=%s&time=afefa4", videoId);

        // when
        String wantedVideoId = YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromUrl_urlWithPreQueryParams_validData() {
        // given
        String videoId = "feafaefaef3145251";
        String url = String.format("watch?&time=afefa4&v=%s", videoId);

        // when
        String wantedVideoId = YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromUrl_urlWithSpecialCharsAndQueryParams_validData() {
        // given
        String videoId = "feafaefaef-3145_2-51";
        String url = String.format("watch?&time=afefa4&v=%s&lala=afeafa314_-", videoId);

        // when
        String wantedVideoId = YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test(expected = YoutubeParserException.class)
    public void getVideoIdFromUrl_noVideoId_exceptionThrown() {
        // given
        String url = "watch?&time=afefa4&lala=afeafa314_-";

        // when
        YoutubeUrlUtils.getInstance().getVideoIdFromUrl(url);

        // then
        // Exception
    }

    @Test
    public void createUrlFromVideoId_validVideo_urlReturned() {
        // given
        String videoId = "lae-af_aef__aef";
        String url = String.format("%s/watch?v=%s", YoutubeConstants.YOUTUBE_BASE_URL, videoId);

        // when
        String wantedUrl = YoutubeUrlUtils.getInstance().createUrlFromVideoId(videoId);

        // then
        assertThat(wantedUrl, equalTo(url));
    }

    @Test(expected = YoutubeParserException.class)
    public void createUrlFromVideoId_nullVideoId_exceptionThrown() {
        // given
        String videoId = null;

        // when
        YoutubeUrlUtils.getInstance().createUrlFromVideoId(videoId);

        // then
        // Exception
    }

    @Test
    public void transformVideoUrlListToVideoIdList_validList_validVideoIdsReturned() {
        // given
        List<String> youtubeVideoUrls = new ArrayList<>();
        String videoId1 = "feafaefaef-3145_2-51";
        String videoId2 = "3145_2-51eaffaEFafqafe_";
        youtubeVideoUrls.add(String.format("watch?time=afefa4&v=%s&lala=afeafa314_-", videoId1));
        youtubeVideoUrls.add(String.format("watch?v=%s", videoId2));

        // when
        List<String> wantedVideoIds = YoutubeUrlUtils.getInstance().transformVideoUrlListToVideoIdList(youtubeVideoUrls);

        // then
        assertThat(wantedVideoIds.get(0), equalTo(videoId1));
        assertThat(wantedVideoIds.get(1), equalTo(videoId2));
    }

    @Test
    public void transformVideoUrlListToVideoIdList_firstCorruptSecondValid_validVideoIdReturned() {
        // given
        List<String> youtubeVideoUrls = new ArrayList<>();
        String videoId1 = "feafaefaef-3145_2-51";
        String videoId2 = "3145_2-51eaffaEFafqafe_";
        youtubeVideoUrls.add(String.format("watch?videoe=%s/afae", videoId1));
        youtubeVideoUrls.add(String.format("watch?time=afefa4&v=%s&lala=afeafa314_-", videoId2));

        // when
        List<String> wantedVideoIds = YoutubeUrlUtils.getInstance().transformVideoUrlListToVideoIdList(youtubeVideoUrls);

        // then
        assertThat(wantedVideoIds.size(), equalTo(1));
        assertThat(wantedVideoIds.get(0), equalTo(videoId2));
    }

    @Test
    public void transformVideoUrlListToVideoIdList_emptyList_emptyListReturned() {
        // given
        List<String> youtubeVideoUrls = new ArrayList<>();

        // when
        List<String> wantedVideoIds = YoutubeUrlUtils.getInstance().transformVideoUrlListToVideoIdList(youtubeVideoUrls);

        // then
        assertThat(wantedVideoIds, notNullValue());
        assertThat(wantedVideoIds.size(), equalTo(0));
    }

}
