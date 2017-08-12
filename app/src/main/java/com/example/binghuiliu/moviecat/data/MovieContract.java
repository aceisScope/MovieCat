package com.example.binghuiliu.moviecat.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by binghuiliu on 12/08/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.binghuiliu.moviecat";
    public static final String PATH_MOVIE = "movie";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";

        public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/movie");

        public static Uri buildMovieUriWithId(int id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
    }
}
