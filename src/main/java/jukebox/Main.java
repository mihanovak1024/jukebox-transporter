package jukebox;

import jukebox.googlesheet.*;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;
import jukebox.youtube.YoutubeSearchData;
import jukebox.youtube.YoutubeSearchInfo;
import jukebox.youtube.YoutubeSongData;

import java.util.List;

public class Main {
    private static final String LOCAL_PROPERTIES_FILE_NAME = "localProperties.txt";

    private NetworkDataFetcher youtubeSearchDataFetcher;
    private NetworkDataFetcher youtubeSongDataFetcher;
    private NetworkDataFetcher googleSheetDataFetcher;

    private GoogleSheetDataUpdater googleSheetDataUpdater;

    // TODO: 2019-08-04 create a cron job to start the main() program
    public static void main(String[] args) {
        // TODO: 2019-08-04 get properties from localProperties.txt

        new Main();
    }

    private Main() {
        initComponents();

        start();
    }

    private void initComponents() {
        // TODO: 2019-08-04 create network data implementations
    }

    // TODO: 2019-08-04 optimize everything (concurrency)
    private void start() {
        List<GoogleSheetData> musicDataList = getGoogleSheetData();

        for (GoogleSheetData musicData : musicDataList) {
            GoogleSheetStatus musicDataStatus = musicData.getGoogleSheetStatus();
            String artist = musicData.getArtist();
            String song = musicData.getSong();
            switch (musicDataStatus) {
                case NEW:
                    YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo(artist, song);
                    recommendYoutubeSong(youtubeSearchInfo);
                    break;
                case APPROVED:
                    uploadSongToRepository();
                    break;
                case REJECTED:
                    youtubeSearchInfo = new YoutubeSearchInfo(artist, song, musicData.getAllYoutubeVideoLinks());
                    recommendYoutubeSong(youtubeSearchInfo);
                    break;
            }
        }
    }

    private List<GoogleSheetData> getGoogleSheetData() {
        String url = "todo";
        List<GoogleSheetData> googleSheetData = googleSheetDataFetcher.fetchData(url);
        return googleSheetData;
    }

    private void recommendYoutubeSong(YoutubeSearchInfo youtubeSearchInfo) {
        String url = "todo";
        youtubeSearchDataFetcher.fetchDataAsync(url, new NetworkDataCallback<YoutubeSearchData>() {
            public void onDataReceived(YoutubeSearchData data) {
                googleSheetDataUpdater.updateData();
            }

            public void onFailure(String error) {
                // TODO: 2019-08-04 retry?
            }
        });
    }

    private void uploadSongToRepository() {
        String url = "todo";
        youtubeSongDataFetcher.fetchDataAsync(url, new NetworkDataCallback<YoutubeSongData>() {
            public void onDataReceived(YoutubeSongData data) {
                // TODO: 2019-08-04 mp4 to mp3 conversion + artist&title setup
                // TODO: 2019-08-04 upload file to drive + delete locally
                googleSheetDataUpdater.updateData();
                googleSheetDataUpdater.saveSongDetailsToBacklog();
            }

            public void onFailure(String error) {
                // TODO: 2019-08-04 retry?
            }
        });
    }
}
