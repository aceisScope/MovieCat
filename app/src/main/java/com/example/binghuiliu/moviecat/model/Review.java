package com.example.binghuiliu.moviecat.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.binghuiliu.moviecat.R;

/**
 * Created by binghuiliu on 18/08/2017.
 */

public class Review implements Parcelable {
    public String id;
    public String author;
    public String content;
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    public Review() {
    }

    public Review(Context context, JSONObject review) throws JSONException {
        this.id = review.getString(context.getString(R.string.key_id));
        this.author = review.getString(context.getString(R.string.key_author));
        this.content = review.getString(context.getString(R.string.key_content));
        this.url = review.getString(context.getString(R.string.key_url));
    }

    protected Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
