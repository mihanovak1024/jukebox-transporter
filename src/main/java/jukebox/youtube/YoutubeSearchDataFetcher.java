package jukebox.youtube;

import jukebox.Util;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jukebox.youtube.YoutubeSearchParserException.SEARCH_JSON_RESPONSE_PARSE_ERROR;

public class YoutubeSearchDataFetcher implements NetworkDataFetcher<YoutubeSearchInfo, YoutubeSearchData> {

    private static final String VIDEO_LIST_REGEX = "ytInitialData\"] = (\\{\"responseContext\":.*);\n";
    private static final int CONTENT_JSON_RESPONSE_GROUP = 1;

    private Pattern regexPattern = Pattern.compile(VIDEO_LIST_REGEX);
    private YoutubeService youtubeService;

    public YoutubeSearchDataFetcher() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(YoutubeConstants.YOUTUBE_BASE_URL)
                .build();

        youtubeService = retrofit.create(YoutubeService.class);
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
        String query = createArtistSongQuery(youtubeSearchInfo);
        Call<String> searchResultsHtmlCall = youtubeService.getSearchResult(query);

        String searchResultHtml = null;
        Response<String> searchResultsHtmlResponse = searchResultsHtmlCall.execute();
        searchResultHtml = searchResultsHtmlResponse.body();

        String requestContextJson = getRequestContextJson(searchResultHtml);
        // TODO: 2019-09-04 transform with Jackson
        return null;
    }

    private String getRequestContextJson(String searchResultHtml) {
        Matcher matcher = regexPattern.matcher(searchResultHtml);
        if (matcher.find()) {
            String responseJson = matcher.group(CONTENT_JSON_RESPONSE_GROUP);
            return responseJson;
        }
        throw new YoutubeSearchParserException(SEARCH_JSON_RESPONSE_PARSE_ERROR);
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
