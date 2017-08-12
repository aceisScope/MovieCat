package com.example.binghuiliu.moviecat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binghuiliu.moviecat.data.Movie;
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

    private String movieId = null;
    private Movie movieDetails = null;

    @BindView(R.id.text_title) TextView titleTextView;
    @BindView(R.id.text_overview) TextView overviewTextView;
    @BindView(R.id.text_rate) TextView rateTextView;
    @BindView(R.id.text_release) TextView releaseTextView;
    @BindView(R.id.image_poster) ImageView posterImageView;
    @BindView(R.id.text_error) TextView errorTextView;

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
    }


    public void displayMovieDetails() {
        Log.d(GlobalConstants.DEBUG, movieDetails.toString());
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
}
