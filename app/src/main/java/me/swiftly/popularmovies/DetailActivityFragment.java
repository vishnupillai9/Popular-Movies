package me.swiftly.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

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
    @Bind(R.id.app_bar) AppBarLayout appBar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.overview_label) TextView overviewLabel;
    @Bind(R.id.detail_label) TextView detailLabel;
    @Bind(R.id.overview_card_view) CardView overviewCardView;
    @Bind(R.id.detail_card_view) CardView detailCardView;

    TMDBMovie movie;
    Integer colorPrimary, colorPrimaryDark, colorPrimaryLight;
    ProgressDialog progressDialog;
    boolean isFavorite = false;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        Intent intent = getActivity().getIntent();
        movie = intent.getParcelableExtra(getString(R.string.detail_intent_extra_name));

        toolbarLayout.setTitle(movie.title);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.show();

        Picasso.with(getActivity().getApplicationContext()).load(movie.backdropPath).into(backdropImageView,
                PicassoPalette.with(movie.backdropPath, backdropImageView)
                        .intoCallBack(
                                new PicassoPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        HashMap<PaletteHelper.Color, Integer> colors = PaletteHelper.getColorsFromPalette(getActivity(), palette);
                                        colorPrimary = colors.get(PaletteHelper.Color.PRIMARY);
                                        colorPrimaryDark = colors.get(PaletteHelper.Color.DARK);
                                        colorPrimaryLight = colors.get(PaletteHelper.Color.LIGHT);

                                        colorUI();

                                        overviewCardView.setVisibility(View.VISIBLE);
                                        detailCardView.setVisibility(View.VISIBLE);

                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    }
                                }
                        )
        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
                    Snackbar.make(getView(), getString(R.string.add_to_favorites_message), Snackbar.LENGTH_SHORT).show();
                    isFavorite = true;
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_48dp));
                    Snackbar.make(getView(), getString(R.string.remove_from_favorites_message), Snackbar.LENGTH_SHORT).show();
                    isFavorite = false;
                }
            }
        });

        overviewTextView.setText(movie.overview);
        movieRatingBar.setRating((float) movie.voteAverage);
        releaseDateTextView.setText(movie.releaseDate);

        return rootView;
    }

    void colorUI() {
        appBar.setBackgroundColor(colorPrimary);

        toolbarLayout.setContentScrimColor(colorPrimary);
        toolbarLayout.setStatusBarScrimColor(colorPrimaryDark);

        fab.setBackgroundTintList(ColorStateList.valueOf(colorPrimaryLight));

        overviewLabel.setTextColor(colorPrimaryDark);
        detailLabel.setTextColor(colorPrimaryDark);

        LayerDrawable stars = (LayerDrawable) movieRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);
    }
}
