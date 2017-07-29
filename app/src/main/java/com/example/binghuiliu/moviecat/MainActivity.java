package com.example.binghuiliu.moviecat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.binghuiliu.moviecat.helpers.EndlessRecyclerOnScrollListener;
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

    private static final int INIT_PAGE = 1;
    private static final int NUMBER_OF_COLOMNS = 2;

    ArrayList<JSONObject> movies = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        loadMoviesData(INIT_PAGE);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLOMNS);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(DEBUG, "onLoadMore: " + page + " " + totalItemsCount);
                loadMoviesData(++page);
            }
        });
    }

    private void loadMoviesData(int page) {
        new WebTask(page).execute(getString(R.string.sort_popular));
    }

    @Override
    public void onItemClick(int position) {
        Log.d(DEBUG, "Click on " + Integer.toString(position));
    }

    private class WebTask extends AsyncTask<String, Void, JSONObject> {

        private int mPage = 1;

        WebTask(int page) {
            this.mPage = page;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            NetworkUtils networkUtils = new NetworkUtils(MainActivity.this);
            String urlString = networkUtils.discoverUrlSortBy(params[0], mPage);
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
