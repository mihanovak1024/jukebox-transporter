package jukebox.youtube;

import jukebox.Util;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;
import jukebox.youtube.response.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jukebox.youtube.YoutubeParserException.*;

public class YoutubeSearchDataFetcher implements NetworkDataFetcher<YoutubeSearchInfo, YoutubeSearchData> {

    private YoutubeService youtubeService;
    private YoutubeSearchResponseParser youtubeSearchResponseParser;

    public YoutubeSearchDataFetcher(YoutubeSearchResponseParser youtubeSearchResponseParser) {
        this.youtubeSearchResponseParser = youtubeSearchResponseParser;
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(YoutubeConstants.YOUTUBE_BASE_URL)
                .build();

        this.youtubeService = retrofit.create(YoutubeService.class);
    }

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
        // TODO: 2019-09-04 change this
        try {
            return createRequest(youtubeSearchInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private YoutubeSearchData createRequest(YoutubeSearchInfo youtubeSearchInfo) throws IOException {
        String songSearchQuery = createArtistSongQuery(youtubeSearchInfo);
        Call<String> searchResultsHtmlCall = youtubeService.getSearchResult(songSearchQuery);

        Response<String> searchResultsHtmlResponse = searchResultsHtmlCall.execute();
        String searchResultHtml = searchResultsHtmlResponse.body();

        return youtubeSearchResponseParser.createYoutubeSearchDataFromResponse(searchResultHtml, youtubeSearchInfo);
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
        throw new YoutubeParserException(SEARCH_QUERY_CREATION_ERROR);
    }
}
