package me.swiftly.popularmovies;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vishnu on 19/04/16.
 */
public class OverviewFragment extends Fragment {
    @Bind(R.id.overview_text_view) TextView overviewTextView;
    @Bind(R.id.movie_rating_bar) RatingBar movieRatingBar;
    @Bind(R.id.release_date_text_view) TextView releaseDateTextView;
    @Bind(R.id.overview_label) TextView overviewLabel;
    @Bind(R.id.detail_label) TextView detailLabel;
    @Bind(R.id.overview_card_view) CardView overviewCardView;
    @Bind(R.id.detail_card_view) CardView detailCardView;

    public static TMDbMovie movie;
    public static Integer colorPrimary, colorPrimaryDark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);

        overviewTextView.setText(movie.overview);
        movieRatingBar.setRating((float) movie.voteAverage);
        releaseDateTextView.setText(movie.releaseDate);

        colorFragment();

        overviewCardView.setVisibility(View.VISIBLE);
        detailCardView.setVisibility(View.VISIBLE);

        return view;
    }

    private void colorFragment() {
        overviewLabel.setTextColor(colorPrimaryDark);
        detailLabel.setTextColor(colorPrimaryDark);

        LayerDrawable stars = (LayerDrawable) movieRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
    }
}
