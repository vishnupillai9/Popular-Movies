package me.swiftly.popularmovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    @Bind(R.id.movies_grid_view) GridView moviesGridView;

    List<TMDbMovie> movies;
    ImageAdapter adapter;
    Call<TMDbResponse> call;
    ProgressDialog progressDialog;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        movies = new ArrayList<TMDbMovie>();

        adapter = new ImageAdapter(getActivity(), movies);
        moviesGridView.setAdapter(adapter);

        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                intent.putExtra(getString(R.string.detail_intent_extra_name), movies.get(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Obtain sort preference from shared preferences.
        String sortPreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString(getString(R.string.pref_movie_sort_key), getString(R.string.pref_movie_sort_default_value));

        updateTitle(sortPreference);
        checkConnectionAndRunTask(sortPreference);
    }

    /**
     * Updates action bar title according to sorting preference set.
     * @param sortPreference The sorting preference that's set in the settings activity.
     */
    private void updateTitle(String sortPreference) {
        if (sortPreference.equals(getString(R.string.pref_top_rated))) {
            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.top_rated_title));
        } else {
            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.app_name));
        }
    }

    /**
     * Checks for internet connection and runs FetchMovieTask if available, shows dialog box otherwise.
     */
    private void checkConnectionAndRunTask(final String sortPreference) {
        if (isOnline()) {
            runTask(sortPreference);
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.connection_offline_message)
                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            checkConnectionAndRunTask(sortPreference);
                        }
                    })
                    .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    /**
     * Indicates whether device is online.
     *
     * @return {@code true} if device is connected to the internet or is in the process of getting connected,
     * {@code false} otherwise.
     */
    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    /**
     * Uses Retrofit to get movies from TheMovieDatabase.
     */
    private void runTask(String sortPreference) {
        Retrofit retrofit = TMDbHelper.buildRetrofit();
        TMDbService.MovieApiEndpointInterface apiService = retrofit.create(TMDbService.MovieApiEndpointInterface.class);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.show();

        call = apiService.getMovies(sortPreference, BuildConfig.TMDB_API_KEY);
        call.enqueue(new Callback<TMDbResponse>() {
            @Override
            public void onResponse(Call<TMDbResponse> call, Response<TMDbResponse> response) {
                if (response.isSuccess()) {
                    TMDbResponse responseBody = response.body();
                    movies.addAll(responseBody.results);
                    adapter.notifyDataSetChanged();
                } else if (response.code() == 401) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.app_name)
                                .setMessage(R.string.invalid_api_key_message)
                                .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.app_name)
                            .setMessage(String.format("Error %d", response.code()))
                            .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TMDbResponse> call, Throwable t) {
                Log.e("getMovies threw", t.getMessage());
            }
        });
    }
}
