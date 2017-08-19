package com.example.binghuiliu.moviecat.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.example.binghuiliu.moviecat.MainActivity;
import com.example.binghuiliu.moviecat.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by binghuiliu on 28/07/2017.
 */

public class NetworkUtils {

    public static final String popular = "popular";

    private final String authority = "api.themoviedb.org";
    private final String v3 = "3";
    private final String discover = "discover";
    private final String movie = "movie";
    private final String review = "reviews";
    private final String trailer = "videos";

    private final Context mContext;

    private final String api_key = "api_key";
    private final String sort_by = "sort_by";
    private final String current_page = "page";
    private final String api_key_value;

    public NetworkUtils(Context context) {
        this.mContext = context;
        this.api_key_value = context.getString(R.string.key_value);
    }

    public String discoverUrlSortBy(String sort, int page) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(authority)
                .appendPath(v3)
                .appendPath(movie)
                .appendPath(sort)
                .appendQueryParameter(api_key, api_key_value)
                .appendQueryParameter(current_page, Integer.toString(page));

        String myURL = builder.build().toString();

        return myURL;
    }

    public String movieDetailUrlBy(String id) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(authority)
                .appendPath(v3)
                .appendPath(movie)
                .appendPath(id)
                .appendQueryParameter(api_key, api_key_value);

        String myURL = builder.build().toString();

        return myURL;
    }

    public String movieReviewUrlBy(String id, int page) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(authority)
                .appendPath(v3)
                .appendPath(movie)
                .appendPath(id)
                .appendPath(review)
                .appendQueryParameter(api_key, api_key_value)
                .appendQueryParameter(current_page, Integer.toString(page));

        String myURL = builder.build().toString();

        return myURL;
    }

    public String movieTrailerUrlBy(String id) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(authority)
                .appendPath(v3)
                .appendPath(movie)
                .appendPath(id)
                .appendPath(trailer)
                .appendQueryParameter(api_key, api_key_value);

        String myURL = builder.build().toString();

        return myURL;
    }

    OkHttpClient client = new OkHttpClient();

    public String getResponseFromHttpUrl(URL url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    private static final String BASE_POST_URL = "http://image.tmdb.org/t/p/w342";

    public static String getPostUrl(String posterPath) {
        return BASE_POST_URL + posterPath;
    }

    private static final String BASE_THUMBNAIL_URL = "https://img.youtube.com/vi/";

    public static String getThumbnailUrl(String key) {
        return BASE_THUMBNAIL_URL + key + "/hqdefault.jpg";
    }

}
