package me.swiftly.popularmovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    @Bind(R.id.movies_grid_view) GridView moviesGridView;

    TmdbMovie[] movies;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: Obtain sort preference from shared preferences.

        final String SORT_BY_POPULARITY = "popular";
        // TODO: Replace with actual sort preference obtained from settings activity.
        checkConnectionAndRunTask(SORT_BY_POPULARITY);
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

    public class FetchMovieTask extends AsyncTask<String, Void, TmdbMovie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getString(R.string.loading_message));
            progressDialog.show();
        }

        @Override
        protected TmdbMovie[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String sortPreference = params[0];

            try {
                URL url = new URL(TmdbHelper.buildUrlForFetchingMovies(sortPreference));

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
                    movies = TmdbHelper.getMoviesFromJson(movieJsonStr);
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
        protected void onPostExecute(final TmdbMovie[] movies) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            moviesGridView.setAdapter(new ImageAdapter(getActivity(), movies));
            moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO: Show detail activity
                }
            });
        }
    }
}
