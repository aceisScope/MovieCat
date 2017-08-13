package com.example.binghuiliu.moviecat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.binghuiliu.moviecat.data.Movie;
import com.example.binghuiliu.moviecat.data.MovieContract;
import com.example.binghuiliu.moviecat.helpers.GlobalConstants;
import com.example.binghuiliu.moviecat.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String DETAIL_MOVIE = "detail_movie";

    private Movie movieDetails = null;

    private boolean isFavorated = false;

    @BindView(R.id.text_title) TextView titleTextView;
    @BindView(R.id.text_overview) TextView overviewTextView;
    @BindView(R.id.text_rate) TextView rateTextView;
    @BindView(R.id.text_release) TextView releaseTextView;
    @BindView(R.id.image_poster) ImageView posterImageView;
    @BindView(R.id.text_error) TextView errorTextView;
    @BindView(R.id.button_favor) Button favorButton;

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

        new QueryFavoriteTask().execute();
    }


    public void displayMovieDetails() {
        titleTextView.setText(movieDetails.title);
        overviewTextView.setText(movieDetails.overView);
        rateTextView.setText(Double.toString(movieDetails.vote_average));
        releaseTextView.setText(movieDetails.release_date);

        String posterURL = NetworkUtils.getPostUrl(movieDetails.poster_path);
        Picasso.with(this).load(posterURL).into(posterImageView);
    }

    public void setDisplaySubviewsInvisible() {
        int visibility = View.INVISIBLE;
        titleTextView.setVisibility(visibility);
        overviewTextView.setVisibility(visibility);
        rateTextView.setVisibility(visibility);
        releaseTextView.setVisibility(visibility);
        posterImageView.setVisibility(visibility);

        errorTextView.setVisibility(View.VISIBLE);
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

}
