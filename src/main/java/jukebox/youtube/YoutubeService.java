package jukebox.youtube;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

interface YoutubeService {

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
    @GET("results")
    Call<String> getSearchResult(@Query("search_query") String searchQuery);

}
