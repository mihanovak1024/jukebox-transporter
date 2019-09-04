package jukebox.youtube;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface YoutubeService {

    @GET("results")
    Call<String> getSearchResult(@Query("search_query") String searchQuery);

}
