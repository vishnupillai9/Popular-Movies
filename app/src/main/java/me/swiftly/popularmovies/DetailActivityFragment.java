package me.swiftly.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    @Bind(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.detail_view_backdrop_image) ImageView backdropImageView;
    @Bind(R.id.overview_text_view) TextView overviewTextView;
    @Bind(R.id.movie_rating_bar) RatingBar movieRatingBar;
    @Bind(R.id.release_date_text_view) TextView releaseDateTextView;

    TmdbMovie movie;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        Intent intent = getActivity().getIntent();
        movie = (TmdbMovie) intent.getSerializableExtra(getString(R.string.detail_intent_extra_name));

        toolbarLayout.setTitle(movie.title);
        Picasso.with(getActivity().getApplicationContext()).load(movie.backdropPath).into(backdropImageView);

        // TODO: Implement favorite button

        overviewTextView.setText(movie.overview);
        movieRatingBar.setRating((float) movie.voteAverage);
        releaseDateTextView.setText(movie.releaseDate);

        return rootView;
    }
}
