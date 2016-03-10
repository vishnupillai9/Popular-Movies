package me.swiftly.popularmovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    @Bind(R.id.movies_grid_view) GridView moviesGridView;

    List<TMDBMovie> movies;
    ImageAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        movies = new ArrayList<TMDBMovie>();

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
     * Executes FetchMovieTask to get movies from TheMovieDatabase.
     */
    private void runTask(String sortPreference) {
        FetchMovieTask task = new FetchMovieTask();
        task.execute(sortPreference);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<TMDBMovie>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getString(R.string.loading_message));
            progressDialog.show();
        }

        @Override
        protected List<TMDBMovie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String sortPreference = params[0];

            try {
                URL url = new URL(TMDBHelper.buildUrlForFetchingMovies(sortPreference));

                // Create the request to TheMovieDatabase, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieJsonStr = buffer.toString();

                try {
                    movies = TMDBHelper.getMoviesFromJson(movieJsonStr);
                    return movies;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(final List<TMDBMovie> m) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            // TODO: Set adapter in onCreateView, and call notifyDataSetChanged here
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
        }
    }
}
