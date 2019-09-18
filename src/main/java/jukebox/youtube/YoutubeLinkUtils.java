package jukebox.youtube;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeLinkUtils {

    private static final int VIDEO_ID_GROUP = 1;
    private static final Pattern VIDEO_ID_REGEX_PATTERN = Pattern.compile("watch.*video=([a-zA-Z0-9]+)");

    private String getVideoIdFromLink(String link) throws YoutubeParserException {
        Matcher matcher = VIDEO_ID_REGEX_PATTERN.matcher(link);
        if (matcher.find()) {
            return matcher.group(VIDEO_ID_GROUP);
        }
        throw new YoutubeParserException(YoutubeParserException.LINK_VIDEO_ID_PARSE_ERROR);
    }

    private String createLinkFromVideoId(String videoId) {
        return String.format(YoutubeConstants.VIDEO_URL_FORMAT, videoId);
    }
}
