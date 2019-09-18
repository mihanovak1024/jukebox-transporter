package jukebox.youtube;

import jukebox.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeLinkUtils {

    private static final int VIDEO_ID_GROUP1 = 1;
    private static final int VIDEO_ID_GROUP2 = 2;
    private static final Pattern VIDEO_ID_REGEX_PATTERN = Pattern.compile("watch.*video=(.+)[/&]|watch.*video=(.*)");

    private static YoutubeLinkUtils INSTANCE;

    static YoutubeLinkUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new YoutubeLinkUtils();
        }
        return INSTANCE;
    }

    String getVideoIdFromLink(String link) throws YoutubeParserException {
        if (Util.isNullOrEmpty(link)) {
            throw new YoutubeParserException(YoutubeParserException.LINK_VIDEO_ID_PARSE_ERROR);
        }
        Matcher matcher = VIDEO_ID_REGEX_PATTERN.matcher(link);
        if (matcher.find()) {
            String videoId = matcher.group(VIDEO_ID_GROUP1);
            if (Util.isNullOrEmpty(videoId)) {
                videoId = matcher.group(VIDEO_ID_GROUP2);
            }
            if (!Util.isNullOrEmpty(videoId)) {
                return videoId;
            }
        }
        throw new YoutubeParserException(YoutubeParserException.LINK_VIDEO_ID_PARSE_ERROR);
    }

    String createLinkFromVideoId(String videoId) {
        if (Util.isNullOrEmpty(videoId)) {
            throw new YoutubeParserException(YoutubeParserException.LINK_VIDEO_ID_PARSE_ERROR);
        }
        return String.format(YoutubeConstants.VIDEO_URL_FORMAT, videoId);
    }

    List<String> transformVideoLinkListToVideoIdList(List<String> youtubeVideoLinks) {
        List<String> youtubeVideoIds = new ArrayList<>();
        for (String youtubeVideoLink : youtubeVideoLinks) {
            try {
                String youtubeVideoId = getVideoIdFromLink(youtubeVideoLink);
                youtubeVideoIds.add(youtubeVideoId);
            } catch (YoutubeParserException e) {
                System.out.println(String.format("Error while parsing link [%s]", youtubeVideoLink));
            }
        }
        return youtubeVideoIds;
    }
}
