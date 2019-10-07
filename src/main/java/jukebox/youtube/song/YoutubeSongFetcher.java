package jukebox.youtube.song;

import jukebox.googlesheet.GoogleSheetData;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;
import jukebox.youtube.YoutubeService;
import jukebox.youtube.YoutubeUrlUtils;
import retrofit2.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class YoutubeSongFetcher implements NetworkDataFetcher<GoogleSheetData, YoutubeSongData> {

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

            YoutubeSongDecipherer youtubeSongDecipherer = new YoutubeSongDecipherer();
            url = youtubeSongDecipherer.getVideoDownloadLink(videoInfo);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 2019-10-05 handle this better
        }
        return url;
    }

    private YoutubeSongData downloadYoutubeSong(String songUrl) {
        // TODO: 2019-10-03 implement
        return null;
    }
}
