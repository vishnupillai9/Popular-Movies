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
public class TrailersFragment extends Fragment {
    @Bind(R.id.trailers_recycler_view) RecyclerView recyclerView;

    public static TMDbMovie movie;
    List<TMDbTrailer> trailers;
    TrailersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        getTrailers();

        return view;
    }

    void getTrailers() {
        Call<TMDbTrailerResponse> call = TMDbHelper.getApiService().getTrailers(movie.id, BuildConfig.TMDB_API_KEY);
        call.enqueue(new Callback<TMDbTrailerResponse>() {
            @Override
            public void onResponse(Call<TMDbTrailerResponse> call, Response<TMDbTrailerResponse> response) {
                if (response.isSuccess()) {
                    TMDbTrailerResponse responseBody = response.body();
                    trailers = responseBody.results;

                    adapter = new TrailersAdapter(getContext(), trailers);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<TMDbTrailerResponse> call, Throwable t) {
                Log.e("getTrailers threw", t.getMessage());
            }
        });
    }
}