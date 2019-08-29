package jukebox.youtube;

import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;

import java.util.concurrent.ExecutorService;

public class YoutubeSearchDataFetcher implements NetworkDataFetcher<YoutubeSearchInfo, YoutubeSearchData> {

    @Override
    public void fetchDataAsync(YoutubeSearchInfo requestData, NetworkDataCallback<YoutubeSearchData> callback, ExecutorService executorService) {

    }

    @Override
    public YoutubeSearchData fetchData(YoutubeSearchInfo requestData) {
        return null;
    }

    private String createUrl() {
        return "";
    }
}
