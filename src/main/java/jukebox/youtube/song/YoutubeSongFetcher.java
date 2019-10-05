package jukebox.youtube.song;

import jukebox.Util;
import jukebox.googlesheet.GoogleSheetData;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;
import jukebox.youtube.YoutubeService;
import jukebox.youtube.YoutubeUrlUtils;
import retrofit2.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeSongFetcher implements NetworkDataFetcher<GoogleSheetData, YoutubeSongData> {

    private static final int URL_GROUP = 1;
    private static final String YOUTUBE_INFO_STREAM_URL_REGEX = "url_encoded_fmt_stream_map.*?medium.*?url%3D(.*?)(%26|%2C)";
    private static final Pattern infoStreamUrlPattern = Pattern.compile(YOUTUBE_INFO_STREAM_URL_REGEX);

    private YoutubeService youtubeService;

    public YoutubeSongFetcher(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @Override
    public void fetchDataAsync(GoogleSheetData requestData, NetworkDataCallback<YoutubeSongData> callback, ExecutorService executorService) {
        executorService.execute(() -> {
            YoutubeSongData downloadedSongData = downloadSong(requestData);
            callback.onDataReceived(downloadedSongData);
        });
    }

    @Override
    public YoutubeSongData fetchData(GoogleSheetData requestData) {
        YoutubeSongData downloadedSongData = downloadSong(requestData);
        return downloadedSongData;
    }

    private YoutubeSongData downloadSong(GoogleSheetData googleSheetData) {
        String songMP3FormatUrl = getSongMP4Url(googleSheetData);
        YoutubeSongData downloadedSongData = downloadYoutubeSong(songMP3FormatUrl);
        return downloadedSongData;
    }

    private String getSongMP4Url(GoogleSheetData googleSheetData) {
        String url = null;
        String videoId = YoutubeUrlUtils.getInstance().getVideoIdFromUrl(googleSheetData.getYoutubeVideoUrl());
        try {
            Response<String> videoInfoResponse = youtubeService.getVideoInfo(videoId).execute();
            String videoInfo = videoInfoResponse.body();

            String doubleEncodedUrl = getMP4UrlFromVideoInfo(videoInfo);
            url = doubleDecodeUrl(doubleEncodedUrl);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 2019-10-05 handle this better
        }
        return url;
    }

    private String doubleDecodeUrl(String doubleEncodedUrl) throws UnsupportedEncodingException {
        String encodedUrl = Util.decodeUrl(doubleEncodedUrl);
        return Util.decodeUrl(encodedUrl);
    }

    private String getMP4UrlFromVideoInfo(String videoInfo) {
        Matcher matcher = infoStreamUrlPattern.matcher(videoInfo);
        String url = null;
        if (matcher.find()) {
            url = matcher.group(URL_GROUP);
        }
        return url;
    }

    private YoutubeSongData downloadYoutubeSong(String songUrl) {
        // TODO: 2019-10-03 implement
        return null;
    }
}
