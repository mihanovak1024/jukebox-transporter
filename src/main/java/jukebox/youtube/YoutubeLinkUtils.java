package jukebox.youtube;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: 2019-09-18 add tests
public class YoutubeLinkUtils {

    private static final int VIDEO_ID_GROUP = 1;
    private static final Pattern VIDEO_ID_REGEX_PATTERN = Pattern.compile("watch.*video=([a-zA-Z0-9]+)");

    private static YoutubeLinkUtils INSTANCE;

    static YoutubeLinkUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new YoutubeLinkUtils();
        }
        return INSTANCE;
    }

    String getVideoIdFromLink(String link) throws YoutubeParserException {
        Matcher matcher = VIDEO_ID_REGEX_PATTERN.matcher(link);
        if (matcher.find()) {
            return matcher.group(VIDEO_ID_GROUP);
        }
        throw new YoutubeParserException(YoutubeParserException.LINK_VIDEO_ID_PARSE_ERROR);
    }

    String createLinkFromVideoId(String videoId) {
        return String.format(YoutubeConstants.VIDEO_URL_FORMAT, videoId);
    }

    List<String> transformVideoLinkListToVideoIdList(List<String> youtubeVideoLinks) {
        List<String> youtubeVideoIds = new ArrayList<>();
        for (String youtubeVideoLink : youtubeVideoLinks) {
            try {
                String youtubeVideoId = getVideoIdFromLink(youtubeVideoLink);
                youtubeVideoIds.add(youtubeVideoId);
            } catch (YoutubeParserException e) {
                e.printStackTrace();
            }
        }
        return youtubeVideoIds;
    }
}
