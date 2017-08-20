package com.example.binghuiliu.moviecat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.binghuiliu.moviecat.helpers.GlobalConstants;
import com.example.binghuiliu.moviecat.model.Movie;
import com.example.binghuiliu.moviecat.data.MovieContract;
import com.example.binghuiliu.moviecat.model.Review;
import com.example.binghuiliu.moviecat.model.Trailer;
import com.example.binghuiliu.moviecat.utils.NetworkUtils;
import com.example.binghuiliu.moviecat.view.ReviewRecyclerViewAdaper;
import com.example.binghuiliu.moviecat.view.TrailerRecyclerViewAdaper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String DETAIL_MOVIE = "detail_movie";

    private boolean isFavorated = false;

    private static int mReviewPage = 1;

    private Movie movieDetails = null;
    private ArrayList<Review> reviews = new ArrayList<Review>();
    private ArrayList<Trailer> trailers = new ArrayList<Trailer>();

    private ReviewRecyclerViewAdaper reviewAdapter;
    private TrailerRecyclerViewAdaper trailerAdapter;

    @BindView(R.id.text_title) TextView titleTextView;
    @BindView(R.id.text_overview) TextView overviewTextView;
    @BindView(R.id.text_rate) TextView rateTextView;
    @BindView(R.id.text_release) TextView releaseTextView;
    @BindView(R.id.image_poster) ImageView posterImageView;
    @BindView(R.id.text_error) TextView errorTextView;
    @BindView(R.id.button_favor) Button favorButton;
    @BindView(R.id.review_list) RecyclerView reviewRecyclerView;
    @BindView(R.id.trailer_list) RecyclerView trailerRecyclerView;

    public void setFavorated(Boolean favorated) {
        this.isFavorated = favorated;
        favorButton.setText(favorated ? getString(R.string.button_unfavor) : getString(R.string.button_favor));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(DETAIL_MOVIE)) {
            movieDetails = intent.getParcelableExtra(DETAIL_MOVIE);
            displayMovieDetails();
        }

        initReviewRecyclerView();
        initTrailerRecyclerView();

        if (savedInstanceState != null) {
            persistState(savedInstanceState);
        } else {
            new ReviewWebTask(mReviewPage).execute(Integer.toString(movieDetails.id));
            new TrailerWebTask().execute(Integer.toString(movieDetails.id));
            new QueryFavoriteTask().execute();
        }
    }

    private void persistState(Bundle savedInstanceState) {
        setFavorated(savedInstanceState.getBoolean(getString(R.string.key_favor)));
        trailers = savedInstanceState.getParcelableArrayList(getString(R.string.key_trailers));
        trailerAdapter.setTrailerData(trailers);
        reviews = savedInstanceState.getParcelableArrayList(getString(R.string.key_reviews));
        reviewAdapter.setReviewData(reviews);
    }

    private void initReviewRecyclerView() {
        reviewAdapter = new ReviewRecyclerViewAdaper(this);
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initTrailerRecyclerView() {
        trailerAdapter = new TrailerRecyclerViewAdaper(this);
        trailerRecyclerView.setAdapter(trailerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailerRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void displayMovieDetails() {
        titleTextView.setText(movieDetails.title);
        overviewTextView.setText(movieDetails.overView);
        rateTextView.setText(Double.toString(movieDetails.vote_average));
        releaseTextView.setText(movieDetails.release_date);

        String posterURL = NetworkUtils.getPostUrl(movieDetails.poster_path);
        Picasso.with(this).load(posterURL).into(posterImageView);
    }

    public void favouriteMovie(View view) {
        if (isFavorated) {
            deleteFavoriteMovie();
        } else {
            insertFavortieMovie();
        }
    }

    private void deleteFavoriteMovie() {
        Uri uri = MovieContract.MovieEntry.buildMovieUriWithId(movieDetails.id);

        int row = getContentResolver().delete(uri, null, null);
        if (row > 0) {
            setFavorated(false);
            Toast.makeText(getBaseContext(), getString(R.string.unfavor_description), Toast.LENGTH_SHORT).show();
        }
    }

    private void insertFavortieMovie() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieDetails.id);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movieDetails.vote_count);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movieDetails.title);
        values.put(MovieContract.MovieEntry.COLUMN_POSTER, movieDetails.poster_path);
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieDetails.original_title);
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieDetails.overView);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieDetails.release_date);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieDetails.vote_average);

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        if(uri != null) {
            setFavorated(true);
            Toast.makeText(getBaseContext(), getString(R.string.favor_description), Toast.LENGTH_SHORT).show();
        }
    }

    private class QueryFavoriteTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Uri uri = MovieContract.MovieEntry.buildMovieUriWithId(movieDetails.id);

            Cursor cursor = MovieDetailActivity.this.getContentResolver().query(uri, null, null, null,null);
            if (cursor.getCount() != 0) {
                cursor.close();
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            setFavorated(aBoolean);
        }
    }

    private class ReviewWebTask extends AsyncTask<String, Void, JSONObject> {
        private int mPage = 1;

        ReviewWebTask(int page) {
            this.mPage = page;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            NetworkUtils networkUtils = new NetworkUtils(MovieDetailActivity.this);
            String urlString = networkUtils.movieReviewUrlBy(params[0], mPage);
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
                        reviews.add(new Review(MovieDetailActivity.this, object));
                    }
                    reviewAdapter.setReviewData(reviews);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class TrailerWebTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            NetworkUtils networkUtils = new NetworkUtils(MovieDetailActivity.this);
            String urlString = networkUtils.movieTrailerUrlBy(params[0]);
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
                        trailers.add(new Trailer(MovieDetailActivity.this, object));
                    }
                    trailerAdapter.setTrailerData(trailers);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.key_reviews), reviews);
        outState.putParcelableArrayList(getString(R.string.key_trailers), trailers);
        outState.putBoolean(getString(R.string.key_favor), isFavorated);
        super.onSaveInstanceState(outState);
    }
}
