package com.example.binghuiliu.moviecat.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.binghuiliu.moviecat.MainActivity;
import com.example.binghuiliu.moviecat.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by binghuiliu on 12/08/2017.
 */

public class Movie implements Parcelable {
    public int id;
    public int vote_count;
    public String title;
    public String poster_path;
    public String original_title;
    public String overView;
    public String release_date;
    public double vote_average;

    public Movie(Context context, JSONObject movie) throws JSONException {
        vote_count = movie.getInt(context.getString(R.string.key_vote_count));
        id = movie.getInt(context.getString(R.string.key_id));
        title = movie.getString(context.getString(R.string.key_title));
        original_title = movie.getString(context.getString(R.string.key_original_title));
        poster_path = movie.getString(context.getString(R.string.key_poster));
        overView = movie.getString(context.getString(R.string.key_overview));
        vote_average = movie.getDouble(context.getString(R.string.key_user_rate));
        release_date = movie.getString(context.getString(R.string.key_release_date));
    }

    public Movie() {
    }

    public Movie(Cursor cursor) {
        id = cursor.getInt(MainActivity.INDEX_MOVIE_ID);
        title = cursor.getString(MainActivity.INDEX_TITLE);
        vote_count = cursor.getInt(MainActivity.INDEX_VOTE_COUNT);
        original_title = cursor.getString(MainActivity.INDEX_ORIGINAL_TITLE);
        poster_path = cursor.getString(MainActivity.INDEX_POSTER);
        overView = cursor.getString(MainActivity.INDEX_OVERVIEW);
        vote_average = cursor.getDouble(MainActivity.INDEX_VOTE_AVERAGE);
        release_date = cursor.getString(MainActivity.INDEX_RELEASE_DATE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vote_count);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.original_title);
        dest.writeString(this.overView);
        dest.writeString(this.release_date);
        dest.writeDouble(this.vote_average);
    }

    protected Movie(Parcel in) {
        this.vote_count = in.readInt();
        this.id = in.readInt();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.original_title = in.readString();
        this.overView = in.readString();
        this.release_date = in.readString();
        this.vote_average = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
