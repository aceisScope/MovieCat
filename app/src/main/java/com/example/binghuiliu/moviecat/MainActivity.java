package com.example.binghuiliu.moviecat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.binghuiliu.moviecat.data.Movie;
import com.example.binghuiliu.moviecat.helpers.EndlessRecyclerOnScrollListener;
import com.example.binghuiliu.moviecat.helpers.GlobalConstants;
import com.example.binghuiliu.moviecat.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener{

    @BindView(R.id.movie_list) RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private EndlessRecyclerOnScrollListener onScrollListener;

    private static final int INIT_PAGE = 1;
    private static final int NUMBER_OF_COLOMNS = 2;
    private String sortBy;

    private ArrayList<Movie> movies = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRecyclerView();

        if (savedInstanceState != null) {
            persistState(savedInstanceState);
        } else {
            sortBy = getString(R.string.sort_popular);
            initLoadMovieData();
        }
    }

    private void persistState(Bundle savedInstanceState) {
        sortBy = savedInstanceState.getString(getString(R.string.key_sort_by));
        movies = savedInstanceState.getParcelableArrayList(getString(R.string.key_movies));
        adapter.setMovieData(movies);
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLOMNS);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, this);
        recyclerView.setAdapter(adapter);

        onScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(GlobalConstants.DEBUG, "onLoadMore: " + page + " item count: " + totalItemsCount);
                loadMoviesData(page);
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
    }

    private void initLoadMovieData() {
        movies.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        onScrollListener.resetState();

        loadMoviesData(INIT_PAGE);
    }

    private void loadMoviesData(int page) {
        new WebTask(page).execute(sortBy);
    }

    @Override
    public void onItemClick(int position) {
        Log.d(GlobalConstants.DEBUG, "Click on " + Integer.toString(position));

        Movie movie = movies.get(position);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.DETAIL_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String old_sortBy = sortBy;
        if (item.getItemId() == R.id.item_popularity) {
            sortBy = getString(R.string.sort_popular);
        } else if (item.getItemId() == R.id.item_rating) {
            sortBy = getString(R.string.sort_rate);
        }

        if (!old_sortBy.equals(sortBy)) {
            initLoadMovieData();
        }

        return true;
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
            Log.d(GlobalConstants.DEBUG, "get url:" + urlString);

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
                        movies.add(new Movie(MainActivity.this, object));
                    }
                    adapter.setMovieData(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.key_sort_by), sortBy);
        outState.putParcelableArrayList(getString(R.string.key_movies), movies);
        super.onSaveInstanceState(outState);
    }
}
