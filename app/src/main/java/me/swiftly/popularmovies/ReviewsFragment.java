package me.swiftly.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishnu on 21/04/16.
 */
public class ReviewsFragment extends Fragment {
    @Bind(R.id.reviews_recycler_view) RecyclerView recyclerView;

    public static TMDbMovie movie;
    List<TMDbReview> reviews;
    ReviewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getReviews();

        return view;
    }

    void getReviews() {
        Call<TMDbReviewResponse> call = TMDbHelper.getApiService().getReviews(movie.id, BuildConfig.TMDB_API_KEY);
        call.enqueue(new Callback<TMDbReviewResponse>() {
            @Override
            public void onResponse(Call<TMDbReviewResponse> call, Response<TMDbReviewResponse> response) {
                if (response.isSuccess()) {
                    TMDbReviewResponse responseBody = response.body();
                    reviews = responseBody.results;

                    adapter = new ReviewsAdapter(getContext(), reviews);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<TMDbReviewResponse> call, Throwable t) {
                Log.e("getReviews threw", t.getMessage());
            }
        });
    }
}
