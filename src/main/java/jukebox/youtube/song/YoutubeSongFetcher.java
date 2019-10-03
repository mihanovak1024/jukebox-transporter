package jukebox.youtube.song;

import jukebox.googlesheet.GoogleSheetData;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;

import java.util.concurrent.ExecutorService;

public class YoutubeSongFetcher implements NetworkDataFetcher<GoogleSheetData, YoutubeSongData> {

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
        String songMP3FormatUrl = getSongMP3FormatUrl(googleSheetData);
        YoutubeSongData downloadedSongData = downloadYoutubeSong(songMP3FormatUrl);
        return downloadedSongData;
    }

    private String getSongMP3FormatUrl(GoogleSheetData googleSheetData) {
        // TODO: 2019-10-03 implement
        return null;
    }

    private YoutubeSongData downloadYoutubeSong(String songUrl) {
        // TODO: 2019-10-03 implement
        return null;
    }
}
