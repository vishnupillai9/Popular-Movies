package me.swiftly.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    @Bind(R.id.nested_scroll_view) NestedScrollView scrollView;
    @Bind(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.detail_view_backdrop_image) ImageView backdropImageView;
    @Bind(R.id.app_bar) AppBarLayout appBar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;

    TMDbMovie movie;
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

        OverviewFragment.movie = movie;
        TrailersFragment.movie = movie;
        ReviewsFragment.movie = movie;

        scrollView.setFillViewport(true);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.show();

        String backdropImageUrl = TMDbHelper.buildImageUrlForPoster(movie.backdropPath, 780);
        Picasso.with(getActivity().getApplicationContext()).load(backdropImageUrl).into(backdropImageView,
                PicassoPalette.with(backdropImageUrl, backdropImageView)
                        .intoCallBack(
                                new PicassoPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        HashMap<PaletteHelper.Color, Integer> colors
                                                = PaletteHelper.getColorsFromPalette(getActivity(), palette);
                                        colorPrimary = colors.get(PaletteHelper.Color.PRIMARY);
                                        colorPrimaryDark = colors.get(PaletteHelper.Color.DARK);
                                        colorPrimaryLight = colors.get(PaletteHelper.Color.LIGHT);

                                        OverviewFragment.colorPrimary = colorPrimary;
                                        OverviewFragment.colorPrimaryDark = colorPrimaryDark;

                                        viewPager.setAdapter(new DetailFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                                                getActivity()));
                                        tabLayout.setupWithViewPager(viewPager);

                                        colorFragment();

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

        return rootView;
    }

    void colorFragment() {
        appBar.setBackgroundColor(colorPrimary);

        toolbarLayout.setContentScrimColor(colorPrimary);
        toolbarLayout.setStatusBarScrimColor(colorPrimaryDark);

        tabLayout.setBackgroundColor(colorPrimary);
        fab.setBackgroundTintList(ColorStateList.valueOf(colorPrimaryLight));
    }
}
