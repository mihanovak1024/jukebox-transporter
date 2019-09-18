package jukebox.youtube;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

interface YoutubeService {

    @Headers(YoutubeConstants.HARDCODED_BROWSER_USER_AGENT)
    @GET("results")
    Call<String> getSearchResult(@Query("search_query") String searchQuery);

}
