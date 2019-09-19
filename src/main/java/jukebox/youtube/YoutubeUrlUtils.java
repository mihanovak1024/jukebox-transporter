package jukebox.youtube;

import jukebox.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeUrlUtils {

    private static final int VIDEO_ID_GROUP1 = 1;
    private static final int VIDEO_ID_GROUP2 = 2;
    private static final Pattern VIDEO_ID_REGEX_PATTERN = Pattern.compile("watch.*v=(.+)[/&]|watch.*v=(.*)");

    private static YoutubeUrlUtils INSTANCE;

    static YoutubeUrlUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new YoutubeUrlUtils();
        }
        return INSTANCE;
    }

    String getVideoIdFromUrl(String url) throws YoutubeParserException {
        if (Util.isNullOrEmpty(url)) {
            throw new YoutubeParserException(YoutubeParserException.URL_VIDEO_ID_PARSE_ERROR);
        }
        Matcher matcher = VIDEO_ID_REGEX_PATTERN.matcher(url);
        if (matcher.find()) {
            String videoId = matcher.group(VIDEO_ID_GROUP1);
            if (Util.isNullOrEmpty(videoId)) {
                videoId = matcher.group(VIDEO_ID_GROUP2);
            }
            if (!Util.isNullOrEmpty(videoId)) {
                return videoId;
            }
        }
        throw new YoutubeParserException(YoutubeParserException.URL_VIDEO_ID_PARSE_ERROR);
    }

    String createUrlFromVideoId(String videoId) {
        if (Util.isNullOrEmpty(videoId)) {
            throw new YoutubeParserException(YoutubeParserException.URL_VIDEO_ID_PARSE_ERROR);
        }
        return String.format(YoutubeConstants.VIDEO_URL_FORMAT, videoId);
    }

    List<String> transformVideoUrlListToVideoIdList(List<String> youtubeVideoUrls) {
        List<String> youtubeVideoIds = new ArrayList<>();
        for (String youtubeVideoUrl : youtubeVideoUrls) {
            try {
                String youtubeVideoId = getVideoIdFromUrl(youtubeVideoUrl);
                youtubeVideoIds.add(youtubeVideoId);
            } catch (YoutubeParserException e) {
                System.out.println(String.format("Error while parsing url [%s]", youtubeVideoUrl));
            }
        }
        return youtubeVideoIds;
    }
}
