package com.example.movie.movieproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        String movieId=null;
        if (intent != null && intent.hasExtra("movieId")) {
            movieId = intent.getStringExtra("movieId");
            Log.d(LOG_TAG,"Movie selected : " + movieId);

        }
        if(movieId!=null) {
            new MovieDetailTask(getActivity()).execute(movieId);
        }

        ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_icon_img);
        String imageUri = intent.getStringExtra("imageUri");
        Picasso.with(getActivity()).load(imageUri).into(imageView);


        return rootView;
    }


    class MovieDetailTask extends AsyncTask<String, Void, String[]> {

        Activity context;
        String apiKey;

        public MovieDetailTask(Activity context) {
            this.context = context;
            this.apiKey = context.getResources().getString(R.string.moviedbkey);
        }


        private String[] getData(String queriedData) throws JSONException {
            JSONObject object = new JSONObject(queriedData);
            String overView = object.getString("overview");
            String original_title = object.getString("original_title");
            String releaseDate = object.getString("release_date");
            String duration = object.getString("runtime");
            return new String[]{overView,original_title,releaseDate,duration};
        }


        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieId = null;

            if(params.length>0){
                movieId = params[0];
            }
            try {

                String apiKey = getActivity().getResources().getString(R.string.moviedbkey);
                String MOVIE_QUERY_URL =
                        "http://api.themoviedb.org/3/movie/"+movieId +"?api_key=" + apiKey ;
                String queriedData;

                URL url = new URL(MOVIE_QUERY_URL);

                // Create the request to OpenWeatherMap, and open the connection
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
                queriedData = buffer.toString();

                Log.d(LOG_TAG, "result=" + queriedData);

                return getData(queriedData);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error occured while fetching data", e);
            }

            return null;

        }

        @Override
        protected void onPostExecute(String[] data) {

            TextView overView = (TextView)context.findViewById(R.id.movie_details);
            overView.setText(data[0]);

            TextView title = (TextView)context.findViewById(R.id.movie_title);
            title.setText(data[1]);


//            super.onPostExecute(strings);
        }
    }

    }
