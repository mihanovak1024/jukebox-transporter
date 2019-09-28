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

import static jukebox.youtube.YoutubeParserException.SEARCH_QUERY_CREATION_ERROR;

/**
 * Class responsible for fetching Youtube search request data
 * and returning a non-rejected Youtube song info based on the previous rejected songs.
 */
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
                YoutubeSearchData youtubeSearchData = getSearchDataFromWeb(youtubeSearchInfo);
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
            return getSearchDataFromWeb(youtubeSearchInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fires a request based on {@link YoutubeSearchInfo} and
     * creates the {@link YoutubeSearchData} with not yet rejected Youtube song.
     *
     * @param youtubeSearchInfo
     * @return not yet rejected song info
     * @throws IOException
     */
    private YoutubeSearchData getSearchDataFromWeb(YoutubeSearchInfo youtubeSearchInfo) throws IOException {
        String searchResultHtml = fireRequestAndReturnResponse(youtubeSearchInfo);

        return youtubeSearchResponseParser.createYoutubeSearchDataFromHtmlResponse(searchResultHtml, youtubeSearchInfo);
    }

    /**
     * Creates a request to Youtube and returns the HTML response content.
     *
     * @param youtubeSearchInfo
     * @return html response content
     * @throws IOException
     */
    private String fireRequestAndReturnResponse(YoutubeSearchInfo youtubeSearchInfo) throws IOException {
        String songSearchQuery = createArtistSongQuery(youtubeSearchInfo);
        Call<String> searchResultsHtmlCall = youtubeService.getSearchResult(songSearchQuery);

        Response<String> searchResultsHtmlResponse = searchResultsHtmlCall.execute();
        return searchResultsHtmlResponse.body();
    }

    private String createArtistSongQuery(YoutubeSearchInfo youtubeSearchInfo) {
        String artist = youtubeSearchInfo.getArtist();
        String song = youtubeSearchInfo.getSong();
        if (Util.isNonEmpty(artist)) {
            if (Util.isNonEmpty(song)) {
                return artist + "+" + song;
            } else {
                return artist;
            }
        } else if (Util.isNonEmpty(song)) {
            return song;
        }
        throw new YoutubeParserException(SEARCH_QUERY_CREATION_ERROR);
    }
}
