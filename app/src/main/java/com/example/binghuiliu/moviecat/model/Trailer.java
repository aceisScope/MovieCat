package com.example.binghuiliu.moviecat.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.example.binghuiliu.moviecat.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by binghuiliu on 19/08/2017.
 */

public class Trailer implements Parcelable {
    public String key;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
    }

    public Trailer() {
    }

    public Trailer(Context context, JSONObject object) throws JSONException {
        this.key = object.getString(context.getString(R.string.key_key));
        this.name = object.getString(context.getString(R.string.key_name));
    }

    protected Trailer(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
