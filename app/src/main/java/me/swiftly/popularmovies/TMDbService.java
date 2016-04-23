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
        Call<TMDbMovieResponse> getMovies(@Path("sort") String sort, @Query("api_key") String apiKey);

        @GET("movie/{id}/videos")
        Call<TMDbTrailerResponse> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

        @GET("movie/{id}/reviews")
        Call<TMDbReviewResponse> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
    }
}
