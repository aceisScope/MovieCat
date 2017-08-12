package com.example.binghuiliu.moviecat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by binghuiliu on 12/08/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "movie.db";

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sqlCreate = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POSTER + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT);";
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
