package com.example.binghuiliu.moviecat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.binghuiliu.moviecat.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener{

    private final String DEBUG = "DEBUG";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_list);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new RecyclerViewAdapter(this, null, this);
        recyclerView.setAdapter(adapter);

        NetworkUtils networkUtils = new NetworkUtils(this);
        String url = networkUtils.discoverUrlSortBy(getString(R.string.sort_popular));
        Log.d(DEBUG, url);
    }

    @Override
    public void onItemClick(int position) {
        Log.d(DEBUG, "Click on " + Integer.toString(position));
    }
}
