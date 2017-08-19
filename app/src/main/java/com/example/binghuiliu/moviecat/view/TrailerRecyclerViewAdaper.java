package com.example.binghuiliu.moviecat.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binghuiliu.moviecat.R;
import com.example.binghuiliu.moviecat.model.Review;
import com.example.binghuiliu.moviecat.model.Trailer;
import com.example.binghuiliu.moviecat.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by binghuiliu on 19/08/2017.
 */

public class TrailerRecyclerViewAdaper extends RecyclerView.Adapter<TrailerRecyclerViewAdaper.TrailerViewHolder> {

    public ArrayList<Trailer> trailerData;

    private final LayoutInflater mLayoutInflater;

    private final Context mContext;

    public TrailerRecyclerViewAdaper(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.trailer_recyclerview_item, parent, false);
        TrailerRecyclerViewAdaper.TrailerViewHolder viewHolder = new TrailerRecyclerViewAdaper.TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = trailerData.get(position);
        Picasso.with(mContext).load(NetworkUtils.getThumbnailUrl(trailer.key)).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (trailerData == null) return 0;
        return trailerData.size();
    }

    public void setTrailerData(ArrayList<Trailer> data) {
        this.trailerData = data;
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        public @BindView(R.id.trailer_thumbnail_image) ImageView thumbnail;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
