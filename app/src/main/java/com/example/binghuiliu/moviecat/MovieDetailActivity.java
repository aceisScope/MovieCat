package com.example.binghuiliu.moviecat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binghuiliu.moviecat.helpers.GlobalConstants;
import com.example.binghuiliu.moviecat.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private String movieId = null;
    private JSONObject movieDetails = null;

    private TextView titleTextView;
    private TextView overviewTextView;
    private TextView rateTextView;
    private TextView releaseTextView;
    private ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        titleTextView = (TextView) findViewById(R.id.text_title);
        overviewTextView = (TextView) findViewById(R.id.text_overview);
        rateTextView = (TextView) findViewById(R.id.text_rate);
        releaseTextView = (TextView) findViewById(R.id.text_release);
        posterImageView = (ImageView) findViewById(R.id.image_poster);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
            getMovieDetail();
        }
    }

    private void getMovieDetail() {
        new WebTask().execute(movieId);
    }

    public void displayMovieDetails() {
        try {
            titleTextView.setText(movieDetails.getString(getString(R.string.key_original_title)));
            overviewTextView.setText(movieDetails.getString(getString(R.string.key_overview)));
            rateTextView.setText(movieDetails.getString(getString(R.string.key_user_rate)));
            releaseTextView.setText(movieDetails.getString(getString(R.string.key_release_date)));

            String posterURL = NetworkUtils.getPostUrl(movieDetails.getString(getString(R.string.key_poster)));
            Picasso.with(this).load(posterURL).into(posterImageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class WebTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            NetworkUtils networkUtils = new NetworkUtils(MovieDetailActivity.this);
            String urlString = networkUtils.movieDetailUrlBy(params[0]);
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
                movieDetails = jsonObject;
                displayMovieDetails();
            }
        }
    }
}
