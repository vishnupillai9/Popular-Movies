package me.swiftly.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishnu on 28/02/16.
 */
public class TMDbMovie implements Parcelable {
    int id;
    String title;
    @SerializedName("poster_path")
    String posterPath;
    @SerializedName("backdrop_path")
    String backdropPath;
    String overview;
    @SerializedName("release_date")
    String releaseDate;
    double popularity;
    @SerializedName("vote_average")
    double voteAverage;

    protected TMDbMovie(Parcel in) {
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
    public static final Parcelable.Creator<TMDbMovie> CREATOR = new Parcelable.Creator<TMDbMovie>() {
        @Override
        public TMDbMovie createFromParcel(Parcel in) {
            return new TMDbMovie(in);
        }

        @Override
        public TMDbMovie[] newArray(int size) {
            return new TMDbMovie[size];
        }
    };
}
