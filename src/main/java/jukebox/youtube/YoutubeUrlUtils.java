package jukebox.youtube;

import jukebox.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for extracting video ids from Youtube video urls
 * or creating whole Youtube video urls out of video ids.
 */
public class YoutubeUrlUtils {

    private static final int VIDEO_ID_GROUP1 = 1;
    private static final int VIDEO_ID_GROUP2 = 2;
    /**
     * Regex pattern for extracting the video id from the Youtube video url.
     */
    private static final Pattern VIDEO_ID_REGEX_PATTERN = Pattern.compile("watch.*v=(.+)[/&]|watch.*v=(.*)");

    private static YoutubeUrlUtils INSTANCE;

    public static YoutubeUrlUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new YoutubeUrlUtils();
        }
        return INSTANCE;
    }

    /**
     * Method that extracts the video id from the url and returns it.
     *
     * @param url
     * @return video id
     * @throws YoutubeParserException
     */
    public String getVideoIdFromUrl(String url) throws YoutubeParserException {
        if (Util.isNullOrEmpty(url)) {
            throw new YoutubeParserException(YoutubeParserException.URL_VIDEO_ID_PARSE_ERROR);
        }
        Matcher matcher = VIDEO_ID_REGEX_PATTERN.matcher(url);
        if (matcher.find()) {
            String videoId = matcher.group(VIDEO_ID_GROUP1);
            if (Util.isNonEmpty(videoId)) {
                return videoId;
            } else {
                videoId = matcher.group(VIDEO_ID_GROUP2);
                if (Util.isNonEmpty(videoId)) {
                    return videoId;
                }
            }
        }
        throw new YoutubeParserException(YoutubeParserException.URL_VIDEO_ID_PARSE_ERROR);
    }

    /**
     * Creates the whole Youtube video url from video id.
     *
     * @param videoId
     * @return Youtube video url
     */
    public String createUrlFromVideoId(String videoId) {
        if (Util.isNullOrEmpty(videoId)) {
            throw new YoutubeParserException(YoutubeParserException.URL_VIDEO_ID_PARSE_ERROR);
        }
        return String.format(YoutubeConstants.VIDEO_URL_FORMAT, videoId);
    }

    /**
     * Creates the list of video ids from Youtube video url list.
     *
     * @param youtubeVideoUrls
     * @return Youtube video id list
     */
    public List<String> transformVideoUrlListToVideoIdList(List<String> youtubeVideoUrls) {
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
