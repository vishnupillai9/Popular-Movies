package me.swiftly.popularmovies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishnu on 28/02/16.
 */
public class TMDBHelper {

    public static List<TMDBMovie> getMoviesFromJson(String jsonStr) throws JSONException {
        final String TMDB_RESULTS = "results";
        final String TMDB_ID = "id";
        final String TMDB_TITLE = "title";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_POPULARITY = "popularity";
        final String TMDB_VOTE_AVERAGE = "vote_average";

        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray resultsArray = movieJson.getJSONArray(TMDB_RESULTS);

        List<TMDBMovie> movies = new ArrayList<TMDBMovie>();

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);

            int id = result.getInt(TMDB_ID);
            String title = result.getString(TMDB_TITLE);
            String posterPath = buildImageUrl(result.getString(TMDB_POSTER_PATH), 342);
            String backdropPath = buildImageUrl(result.getString(TMDB_BACKDROP_PATH), 780);
            String overview = result.getString(TMDB_OVERVIEW);
            String releaseDate = result.getString(TMDB_RELEASE_DATE);
            double popularity = result.getDouble(TMDB_POPULARITY);
            double voteAverage = result.getDouble(TMDB_VOTE_AVERAGE);

            TMDBMovie movie = new TMDBMovie(id, title, posterPath, backdropPath, overview,
                    releaseDate, popularity, voteAverage);
            movies.add(movie);
        }

        return movies;
    }

    public static String buildUrlForFetchingMovies(String sortPreference) {
        final String URL_SCHEME = "http";
        final String URL_AUTHORITY = "api.themoviedb.org";
        final String API_VERSION = "3";
        final String SEARCH_TYPE = "movie";
        final String API_KEY_PARAM = "api_key";

        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme(URL_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath(API_VERSION)
                .appendPath(SEARCH_TYPE)
                .appendPath(sortPreference)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        return uri.toString();
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
