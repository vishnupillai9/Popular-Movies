package me.swiftly.popularmovies;

/**
 * Created by vishnu on 28/02/16.
 */
public class TMDbHelper {
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
