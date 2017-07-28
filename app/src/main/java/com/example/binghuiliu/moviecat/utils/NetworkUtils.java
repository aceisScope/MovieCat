package com.example.binghuiliu.moviecat.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.example.binghuiliu.moviecat.MainActivity;
import com.example.binghuiliu.moviecat.R;

import java.net.URL;

/**
 * Created by binghuiliu on 28/07/2017.
 */

public class NetworkUtils {

    public static final String popular = "popular";

    private final String authority = "api.themoviedb.org";
    private final String v3 = "3";
    private final String discover = "discover";
    private final String movile = "movie";

    private final Context mContext;

    private final String api_key = "api_key";
    private final String sort_by = "sort_by";
    private final String api_key_value;

    public NetworkUtils(Context context) {
        this.mContext = context;
        this.api_key_value = context.getString(R.string.key_value);
    }

    public String discoverUrlSortBy(String sort) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(authority)
                .appendPath(v3)
                .appendPath(discover)
                .appendPath(movile)
                .appendQueryParameter(sort_by, sort)
                .appendQueryParameter(api_key, api_key_value);

        String myURL = builder.build().toString();

        return myURL;
    }

}
