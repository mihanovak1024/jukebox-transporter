package jukebox.youtube;

import jukebox.Util;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;

import java.util.concurrent.ExecutorService;

public class YoutubeSearchDataFetcher implements NetworkDataFetcher<YoutubeSearchInfo, YoutubeSearchData> {

    private static final String YOUTUBE_SEARCH_BASE_URL = "https://www.youtube.com/results?search_query=";

    @Override
    public void fetchDataAsync(YoutubeSearchInfo youtubeSearchInfo, NetworkDataCallback<YoutubeSearchData> callback, ExecutorService executorService) {
        executorService.execute(() -> {
            try {
                YoutubeSearchData youtubeSearchData = createRequest(youtubeSearchInfo);
                callback.onDataReceived(youtubeSearchData);
            } catch (Exception e) { // TODO: 2019-08-29 more specific exception
                callback.onFailure("Fetch data async error"); // TODO: 2019-08-29 more specific error
            }
        });
    }

    @Override
    public YoutubeSearchData fetchData(YoutubeSearchInfo youtubeSearchInfo) {
        return createRequest(youtubeSearchInfo);
    }

    private YoutubeSearchData createRequest(YoutubeSearchInfo youtubeSearchInfo) {
        String url = createUrl(youtubeSearchInfo);

        // TODO: 2019-08-29 create actual request + transform with Jackson
        return null;
    }

    private String createUrl(YoutubeSearchInfo youtubeSearchInfo) {
        String searchQuery = createArtistSongQuery(youtubeSearchInfo);
        return YOUTUBE_SEARCH_BASE_URL + searchQuery;
    }

    private String createArtistSongQuery(YoutubeSearchInfo youtubeSearchInfo) {
        String artist = youtubeSearchInfo.getArtist();
        String song = youtubeSearchInfo.getSong();
        if (!Util.isNullOrEmpty(artist)) {
            if (!Util.isNullOrEmpty(song)) {
                return artist + "+" + song;
            } else {
                return artist;
            }
        } else if (!Util.isNullOrEmpty(song)) {
            return song;
        }
        throw new IllegalStateException("Both artist and song are null!");
    }
}
