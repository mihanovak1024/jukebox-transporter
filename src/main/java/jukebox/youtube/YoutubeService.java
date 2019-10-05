package jukebox.youtube;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Retrofit service for creating a request to Youtube.
 */
public interface YoutubeService {

    @Headers(YoutubeConstants.HARDCODED_BROWSER_USER_AGENT)
    @GET("results")
    Call<String> getSearchResult(@Query("search_query") String searchQuery);

    @Headers(YoutubeConstants.HARDCODED_BROWSER_USER_AGENT)
    @GET("get_video_info?el=embedded&ps=default&eurl=&gl=US&hl=en")
    Call<String> getVideoInfo(@Query("video_id") String videoIdQuery);

}
