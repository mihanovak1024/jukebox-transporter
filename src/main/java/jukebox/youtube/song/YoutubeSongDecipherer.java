package jukebox.youtube.song;

import jukebox.Util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class YoutubeSongDecipherer {

    private static final int URL_GROUP = 1;
    private static final String YOUTUBE_INFO_STREAM_URL_REGEX = "url_encoded_fmt_stream_map.*?medium.*?url%3D(.*?)(%26|%2C)";
    private static final Pattern infoStreamUrlPattern = Pattern.compile(YOUTUBE_INFO_STREAM_URL_REGEX);

    String getVideoDownloadLink(String videoInfo) throws UnsupportedEncodingException {
        if (isSongProtected(videoInfo)) {
            return getProtectedUrl(videoInfo);
        } else {
            return getNonProtectedUrl(videoInfo);
        }
    }

    private String getProtectedUrl(String videoInfo) {
        // TODO: 2019-10-07 implement
        return null;
    }

    private String getNonProtectedUrl(String videoInfo) throws UnsupportedEncodingException {
        String doubleEncodedUrl = getMP4UrlFromVideoInfo(videoInfo);
        return doubleDecodeUrl(doubleEncodedUrl);
    }

    private String getMP4UrlFromVideoInfo(String videoInfo) {
        Matcher matcher = infoStreamUrlPattern.matcher(videoInfo);
        String url = null;
        if (matcher.find()) {
            url = matcher.group(URL_GROUP);
        }
        return url;
    }

    private String doubleDecodeUrl(String doubleEncodedUrl) throws UnsupportedEncodingException {
        String encodedUrl = Util.decodeUrl(doubleEncodedUrl);
        return Util.decodeUrl(encodedUrl);
    }

    private boolean isSongProtected(String videoInfo) {
        // TODO: 2019-10-07 implementation
        return false;
    }

    private String decipherSongInfo(String videoInfo) {
        // TODO: 2019-10-07 implementation
        return "";
    }

}
