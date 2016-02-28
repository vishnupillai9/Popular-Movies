package me.swiftly.popularmovies;

import java.io.Serializable;

/**
 * Created by vishnu on 28/02/16.
 */
public class TmdbMovie implements Serializable {
    int id;
    String title;
    String posterPath;
    String backdropPath;
    String overview;
    String releaseDate;
    double popularity;
    double voteAverage;

    public TmdbMovie(int id, String title, String posterPath, String backdropPath, String overview, String releaseDate, double popularity, double voteAverage) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
    }
}
