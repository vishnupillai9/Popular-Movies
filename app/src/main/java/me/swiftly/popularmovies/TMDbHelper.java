package me.swiftly.popularmovies;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vishnu on 28/02/16.
 */
public class TMDbHelper {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static String buildImageUrl(String path, int resolution) {
        final String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String TMDB_IMAGE_RESOLUTION_342 = "w342";
        final String TMDB_IMAGE_RESOLUTION_780 = "w780";

        switch (resolution) {
            case 780: return TMDB_IMAGE_BASE_URL + TMDB_IMAGE_RESOLUTION_780 + path;
            case 342:
            default: return TMDB_IMAGE_BASE_URL + TMDB_IMAGE_RESOLUTION_342 + path;
        }
    }
}
