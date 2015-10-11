package com.example.movie.movieproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.example.movie.movieproject.adapter.FragmentData;
import com.example.movie.movieproject.adapter.FragmentListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String LOG_TAG = MainActivityFragment.class.getSimpleName();

    FragmentListAdapter adapter;

    static int pagesFetched = 0;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<FragmentData> testData = new ArrayList<FragmentData>();

        adapter = new FragmentListAdapter(getActivity(), R.layout.fragment_list_single, testData);

        GridView listView = (GridView) rootView.findViewById(R.id.listView);

        listView.setAdapter(adapter);
        listView.setOnScrollListener(new ListViewScrollListener(listView));

        return rootView;
    }

    class ListViewScrollListener implements AbsListView.OnScrollListener {

        private String LOG_TAG = ListViewScrollListener.class.getSimpleName();
        int pageFetched = 1;
        int previousTotalCount;

        GridView listView;

        public ListViewScrollListener(GridView listView) {
            this.listView = listView;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != previousTotalCount) {
                previousTotalCount = totalItemCount;
                pageFetched++;
                fetchNextPage(pageFetched);
            }

        }

    }


    void fetchNextPage(int i) {
        if (i > pagesFetched) {
            pagesFetched++;
            Log.d(LOG_TAG, "Fetching page " + pagesFetched);
            new FetchMovieInformationTask(getActivity()).execute(Integer.toString(pagesFetched));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchNextPage(1);
    }

    class FetchMovieInformationTask extends AsyncTask<String, Void, FragmentData[]> {

        private String LOG_TAG = FetchMovieInformationTask.class.getSimpleName();
        Activity context;
        String apiKey;

        public FetchMovieInformationTask(Activity context) {
            this.context = context;
            this.apiKey = context.getResources().getString(R.string.moviedbkey);
        }


        private FragmentData[] getFragmentData(String jsonData) throws JSONException {
            JSONObject data = new JSONObject(jsonData);
            JSONArray resultList = data.getJSONArray("results");
            FragmentData[] fragmentDataList = new FragmentData[resultList.length()];
            for (int i = 0; i < resultList.length(); i++) {
                JSONObject resultObject = resultList.getJSONObject(i);
                String title = resultObject.getString("original_title");
                String poster_path = resultObject.getString("poster_path");
                String imageBaseURL = "http://image.tmdb.org/t/p/w500/";
                String imageURL = imageBaseURL + poster_path;
                fragmentDataList[i] = new FragmentData(imageURL, title);
            }

            return fragmentDataList;

        }

        @Override
        protected FragmentData[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String pgNumber = "1";
            if (params.length > 0) {
                pgNumber = params[0];
            }
            try {

                String apiKey = getActivity().getResources().getString(R.string.moviedbkey);
                String MOVIE_QUERY_URL =
                        "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + apiKey + "&page=" + pgNumber;
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

                return getFragmentData(queriedData);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error occured while fetching data", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(FragmentData[] fragmentDatas) {
            //adapter.clear();
            for (FragmentData data : fragmentDatas) {
                adapter.add(data);
            }
        }
    }

}
