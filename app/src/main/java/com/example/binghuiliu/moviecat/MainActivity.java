package com.example.binghuiliu.moviecat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.binghuiliu.moviecat.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener{

    private final String DEBUG = "DEBUG";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    ArrayList<JSONObject> movies = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_list);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new RecyclerViewAdapter(this, this);
        recyclerView.setAdapter(adapter);

        loadMoviesData();
    }

    private void loadMoviesData() {
        new WebTask().execute(getString(R.string.sort_popular));
    }

    @Override
    public void onItemClick(int position) {
        Log.d(DEBUG, "Click on " + Integer.toString(position));
    }

    private class WebTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            NetworkUtils networkUtils = new NetworkUtils(MainActivity.this);
            String urlString = networkUtils.discoverUrlSortBy(params[0]);
            Log.d(DEBUG, urlString);

            try {
                URL url = new URL(urlString);
                String result = networkUtils.getResponseFromHttpUrl(url);

                JSONObject jObject = new JSONObject(result);
                return jObject;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.key_results));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        movies.add(object);
                    }
                    adapter.setMovieData(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
