package jukebox;

import jukebox.googlesheet.*;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;
import jukebox.network.NetworkDataUpdater;
import jukebox.youtube.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final String LOCAL_PROPERTIES_FILE_NAME = "/localProperties.json";
    private static final int EXECUTOR_THREAD_POOL_SIZE = 5;

    private ExecutorService executorService = Executors.newFixedThreadPool(EXECUTOR_THREAD_POOL_SIZE);

    private NetworkDataFetcher<YoutubeSearchInfo, YoutubeSearchData> youtubeSearchDataFetcher;
    private NetworkDataFetcher<String, YoutubeSongData> youtubeSongDataFetcher; // TODO: 2019-08-29 change String to actual request object if needed
    private NetworkDataFetcher<Object, List<GoogleSheetData>> googleSheetDataFetcher;

    private NetworkDataUpdater<GoogleSheetData> googleSheetDataUpdater;

    // TODO: 2019-08-04 create a cron job to start the main() program
    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        LocalProperties localProperties = readLocalPropertiesFromFile();

        initComponents(localProperties);
        start();
    }

    private LocalProperties readLocalPropertiesFromFile() {
        try {
            LocalProperties localProperties = Util.readLocalJSONFileToObject(LOCAL_PROPERTIES_FILE_NAME, LocalProperties.class);
            return localProperties;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(String.format("Error while parsing [%s]", LOCAL_PROPERTIES_FILE_NAME), e.getCause());
        }
    }

    private void initComponents(LocalProperties localProperties) {
        GoogleSheetConnector googleSheetConnector = new GoogleSheetConnector();

        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        googleSheetDataFetcher = new GoogleSheetDataFetcher(googleSheetDataParser, googleSheetConnector, localProperties);

        YoutubeSearchResponseParser youtubeSearchResponseParser = new YoutubeSearchResponseParser();
        youtubeSearchDataFetcher = new YoutubeSearchDataFetcher(youtubeSearchResponseParser);

        googleSheetDataUpdater = new GoogleSheetDataUpdater(googleSheetConnector, localProperties);
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
                    YoutubeSearchInfo youtubeSearchInfo = new YoutubeSearchInfo
                            .YoutubeSearchInfoBuilder()
                            .artist(artist)
                            .song(song)
                            .build();
                    recommendYoutubeSong(musicData, youtubeSearchInfo);
                    break;
                case APPROVED:
                    uploadSongToRepository(musicData);
                    break;
                case REJECTED:
                    youtubeSearchInfo = new YoutubeSearchInfo
                            .YoutubeSearchInfoBuilder()
                            .artist(artist)
                            .song(song)
                            .previousUrls(musicData.getAllYoutubeVideoUrls())
                            .build();
                    recommendYoutubeSong(musicData, youtubeSearchInfo);
                    break;
            }
        }
    }

    private List<GoogleSheetData> getGoogleSheetData() {
        List<GoogleSheetData> googleSheetData = googleSheetDataFetcher.fetchData(null);
        return googleSheetData;
    }

    private void recommendYoutubeSong(GoogleSheetData googleSheetData, YoutubeSearchInfo youtubeSearchInfo) {
        youtubeSearchDataFetcher.fetchDataAsync(youtubeSearchInfo, new NetworkDataCallback<>() {
            public void onDataReceived(YoutubeSearchData youtubeSearchData) {
                GoogleSheetData updatedData = updateSearchData(googleSheetData, youtubeSearchData);
                googleSheetDataUpdater.updateData(updatedData);
            }

            public void onFailure(String error) {
                // TODO: 2019-08-04 retry?
            }
        }, executorService);
    }

    private void uploadSongToRepository(GoogleSheetData googleSheetData) {
        // TODO: 2019-08-29 request url/object
        String url = "todo";
        youtubeSongDataFetcher.fetchDataAsync(url, new NetworkDataCallback<>() {
            public void onDataReceived(YoutubeSongData youtubeSongData) {
                // TODO: 2019-08-04 mp4 to mp3 conversion + artist&title setup
                // TODO: 2019-08-04 upload file to drive + delete locally

                // TODO: 2019-09-22 remove googleSheetData entry (update status to "terminate"?)
                googleSheetDataUpdater.updateData(googleSheetData);
                googleSheetDataUpdater.saveSongDetailsToBacklog();
            }

            public void onFailure(String error) {
                // TODO: 2019-08-04 retry?
            }
        }, executorService);
    }

    private GoogleSheetData updateSearchData(GoogleSheetData googleSheetData, YoutubeSearchData youtubeSearchData) {
        List<String> youtubeUrls = googleSheetData.getAllYoutubeVideoUrls();
        youtubeUrls.add(youtubeSearchData.getYoutubeUrl());

        GoogleSheetData updatedGoogleSheetData = new GoogleSheetData(
                GoogleSheetStatus.PENDING,
                googleSheetData.getArtist(),
                googleSheetData.getSong(),
                youtubeSearchData.getTitle(),
                youtubeSearchData.getYoutubeUrl(),
                googleSheetData.getDirectory(),
                youtubeUrls,
                googleSheetData.getIndex()
        );

        return updatedGoogleSheetData;
    }
}
