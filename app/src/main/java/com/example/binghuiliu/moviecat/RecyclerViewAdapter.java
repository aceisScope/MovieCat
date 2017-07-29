package com.example.binghuiliu.moviecat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binghuiliu.moviecat.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by binghuiliu on 27/07/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieViewHolder>{

    public ArrayList<JSONObject> movieData;

    private final LayoutInflater mLayoutInflater;

    private final Context mContext;

    private OnItemClickListener mClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RecyclerViewAdapter(Context context,  OnItemClickListener listener) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        JSONObject jsonObject = movieData.get(position);
        try {
            String title = jsonObject.getString(mContext.getString(R.string.key_original_title));
            holder.myTextView.setText(title);

            String posterURL = NetworkUtils.getPostUrl(jsonObject.getString(mContext.getString(R.string.key_poster)));
            Picasso.with(mContext).load(posterURL).into(holder.mImageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (movieData == null) return 0;
        return movieData.size();
    }

    public void setMovieData(ArrayList<JSONObject> data) {
        this.movieData = data;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView myTextView;

        public ImageView mImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.item_text);
            mImageView = (ImageView) itemView.findViewById(R.id.poster_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onItemClick(getAdapterPosition());
        }
    }
}
