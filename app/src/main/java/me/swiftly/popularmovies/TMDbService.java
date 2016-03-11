package me.swiftly.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by vishnu on 11/03/16.
 */
public class TMDbService {
    public interface MovieApiEndpointInterface {
        @GET("movie/{sort}")
        Call<TMDbResponse> getMovies(
                @Path("sort") String sort,
                @Query("api_key") String apiKey
        );
    }
}
