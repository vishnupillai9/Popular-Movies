package me.swiftly.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vishnu on 28/02/16.
 */
public class TMDbHelper {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static TMDbService.MovieApiEndpointInterface getApiService() {
        return buildRetrofit().create(TMDbService.MovieApiEndpointInterface.class);
    }

    public static String buildImageUrlForPoster(String path, int resolution) {
        final String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String TMDB_IMAGE_RESOLUTION_342 = "w342";
        final String TMDB_IMAGE_RESOLUTION_780 = "w780";

        switch (resolution) {
            case 780: return TMDB_IMAGE_BASE_URL + TMDB_IMAGE_RESOLUTION_780 + path;
            case 342:
            default: return TMDB_IMAGE_BASE_URL + TMDB_IMAGE_RESOLUTION_342 + path;
        }
    }

    public static String buildImageUrlForTrailer(String key) {
        final String YT_IMG_BASE_URL = "https://i.ytimg.com/vi/";
        final String YT_IMG_RES = "/hqdefault.jpg";

        return YT_IMG_BASE_URL + key + YT_IMG_RES;
    }

    public static void playTrailer(Context context, String key) {
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
            context.startActivity(intent);
        }
    }
}
