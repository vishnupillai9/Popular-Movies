package me.swiftly.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vishnu on 28/02/16.
 */
public class TMDBMovie implements Parcelable {
    int id;
    String title;
    String posterPath;
    String backdropPath;
    String overview;
    String releaseDate;
    double popularity;
    double voteAverage;

    public TMDBMovie(int id, String title, String posterPath, String backdropPath, String overview, String releaseDate, double popularity, double voteAverage) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
    }

    protected TMDBMovie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        popularity = in.readDouble();
        voteAverage = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(popularity);
        dest.writeDouble(voteAverage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TMDBMovie> CREATOR = new Parcelable.Creator<TMDBMovie>() {
        @Override
        public TMDBMovie createFromParcel(Parcel in) {
            return new TMDBMovie(in);
        }

        @Override
        public TMDBMovie[] newArray(int size) {
            return new TMDBMovie[size];
        }
    };
}
