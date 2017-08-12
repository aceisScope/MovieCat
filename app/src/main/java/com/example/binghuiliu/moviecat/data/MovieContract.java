package com.example.binghuiliu.moviecat.data;

import android.provider.BaseColumns;

/**
 * Created by binghuiliu on 12/08/2017.
 */

public class MovieContract {
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
    }
}
