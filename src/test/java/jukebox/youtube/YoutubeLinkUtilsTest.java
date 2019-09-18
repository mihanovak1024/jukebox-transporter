package jukebox.youtube;

import jukebox.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class YoutubeLinkUtilsTest extends BaseTest {

    @Test
    public void getVideoIdFromLink_baseLink_validData() {
        // given
        String videoId = "e15115afTT3ta";
        String link = String.format("watch?video=%s", videoId);

        // when
        String wantedVideoId = YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test(expected = YoutubeParserException.class)
    public void getVideoIdFromLink_nullLink_youtubeParseError() {
        // given
        String link = null;

        // when
        YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        // Exception
    }

    @Test
    public void getVideoIdFromLink_linkWithUnderscore_validData() {
        // given
        String videoId = "e15115_afTT_3ta";
        String link = String.format("watch?video=%s", videoId);

        // when
        String wantedVideoId = YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromLink_linkWithDash_validData() {
        // given
        String videoId = "e15115-afTT-3ta";
        String link = String.format("watch?video=%s", videoId);

        // when
        String wantedVideoId = YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromLink_linkWithSlashes_validData() {
        // given
        String videoId = "feafaefaef3145251";
        String link = String.format("watch?video=%s/afefa4", videoId);

        // when
        String wantedVideoId = YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromLink_linkWithQueryParams_validData() {
        // given
        String videoId = "feafaefaef3145251";
        String link = String.format("watch?video=%s&time=afefa4", videoId);

        // when
        String wantedVideoId = YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromLink_linkWithPreQueryParams_validData() {
        // given
        String videoId = "feafaefaef3145251";
        String link = String.format("watch?&time=afefa4&video=%s", videoId);

        // when
        String wantedVideoId = YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test
    public void getVideoIdFromLink_linkWithSpecialCharsAndQueryParams_validData() {
        // given
        String videoId = "feafaefaef-3145_2-51";
        String link = String.format("watch?&time=afefa4&video=%s&lala=afeafa314_-", videoId);

        // when
        String wantedVideoId = YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        assertThat(wantedVideoId, equalTo(videoId));
    }

    @Test (expected = YoutubeParserException.class)
    public void getVideoIdFromLink_noVideoId_exceptionThrown() {
        // given
        String link = "watch?&time=afefa4&lala=afeafa314_-";

        // when
        YoutubeLinkUtils.getInstance().getVideoIdFromLink(link);

        // then
        // Exception
    }

    @Test
    public void createLinkFromVideoId_validVideo_linkReturned() {
        // given
        String videoId = "lae-af_aef__aef";
        String link = String.format("%s/watch?video=%s", YoutubeConstants.YOUTUBE_BASE_URL, videoId);

        // when
        String wantedLink = YoutubeLinkUtils.getInstance().createLinkFromVideoId(videoId);

        // then
        assertThat(wantedLink, equalTo(link));
    }

    @Test(expected = YoutubeParserException.class)
    public void createLinkFromVideoId_nullVideoId_exceptionThrown() {
        // given
        String videoId = null;

        // when
        YoutubeLinkUtils.getInstance().createLinkFromVideoId(videoId);

        // then
        // Exception
    }

    @Test
    public void transformVideoLinkListToVideoIdList_validList_validVideoIdsReturned() {
        // given
        List<String> youtubeVideoLinks = new ArrayList<>();
        String videoId1 = "feafaefaef-3145_2-51";
        String videoId2 = "3145_2-51eaffaEFafqafe_";
        youtubeVideoLinks.add(String.format("watch?time=afefa4&video=%s&lala=afeafa314_-", videoId1));
        youtubeVideoLinks.add(String.format("watch?video=%s", videoId2));

        // when
        List<String> wantedVideoIds = YoutubeLinkUtils.getInstance().transformVideoLinkListToVideoIdList(youtubeVideoLinks);

        // then
        assertThat(wantedVideoIds.get(0), equalTo(videoId1));
        assertThat(wantedVideoIds.get(1), equalTo(videoId2));
    }

    @Test
    public void transformVideoLinkListToVideoIdList_firstCorruptSecondValid_validVideoIdReturned() {
        // given
        List<String> youtubeVideoLinks = new ArrayList<>();
        String videoId1 = "feafaefaef-3145_2-51";
        String videoId2 = "3145_2-51eaffaEFafqafe_";
        youtubeVideoLinks.add(String.format("watch?videoe=%s/afae", videoId1));
        youtubeVideoLinks.add(String.format("watch?time=afefa4&video=%s&lala=afeafa314_-", videoId2));

        // when
        List<String> wantedVideoIds = YoutubeLinkUtils.getInstance().transformVideoLinkListToVideoIdList(youtubeVideoLinks);

        // then
        assertThat(wantedVideoIds.size(), equalTo(1));
        assertThat(wantedVideoIds.get(0), equalTo(videoId2));
    }

    @Test
    public void transformVideoLinkListToVideoIdList_emptyList_emptyListReturned() {
        // given
        List<String> youtubeVideoLinks = new ArrayList<>();

        // when
        List<String> wantedVideoIds = YoutubeLinkUtils.getInstance().transformVideoLinkListToVideoIdList(youtubeVideoLinks);

        // then
        assertThat(wantedVideoIds, notNullValue());
        assertThat(wantedVideoIds.size(), equalTo(0));
    }

}
