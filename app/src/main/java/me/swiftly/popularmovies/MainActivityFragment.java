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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    @Bind(R.id.movies_grid_view) GridView moviesGridView;

    List<TMDbMovie> movies;
    ImageAdapter adapter;
    Call<TMDbMovieResponse> call;
    ProgressDialog progressDialog;
    DialogInterface.OnClickListener dialogOnClickListener;

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
        final String sortPreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString(getString(R.string.pref_movie_sort_key), getString(R.string.pref_movie_sort_default_value));

        updateTitle(sortPreference);

        dialogOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkConnectionAndRunTask(sortPreference);
            }
        };

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
            showAlertDialog(getString(R.string.connection_offline_message), dialogOnClickListener);
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
    private void runTask(final String sortPreference) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.show();

        call = TMDbHelper.getApiService().getMovies(sortPreference, BuildConfig.TMDB_API_KEY);
        call.enqueue(new Callback<TMDbMovieResponse>() {
            @Override
            public void onResponse(Call<TMDbMovieResponse> call, Response<TMDbMovieResponse> response) {
                if (response.isSuccess()) {
                    TMDbMovieResponse responseBody = response.body();
                    movies.addAll(responseBody.results);
                    adapter.notifyDataSetChanged();
                } else if (response.code() == 401) {
                    showAlertDialog(getString(R.string.invalid_api_key_message), dialogOnClickListener);
                } else {
                    showAlertDialog("Error " + response.code(), dialogOnClickListener);
                }

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TMDbMovieResponse> call, Throwable t) {
                Log.e("getMovies threw", t.getMessage());
            }
        });
    }

    private void showAlertDialog(String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(R.string.retry, onClickListener)
                .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
