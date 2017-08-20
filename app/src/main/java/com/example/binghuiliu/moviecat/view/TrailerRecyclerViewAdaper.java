package com.example.binghuiliu.moviecat.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binghuiliu.moviecat.MainActivity;
import com.example.binghuiliu.moviecat.R;
import com.example.binghuiliu.moviecat.helpers.GlobalConstants;
import com.example.binghuiliu.moviecat.model.Review;
import com.example.binghuiliu.moviecat.model.Trailer;
import com.example.binghuiliu.moviecat.utils.NetworkUtils;
import com.squareup.picasso.Callback;
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
    public void onBindViewHolder(final TrailerViewHolder holder, int position) {
        Trailer trailer = trailerData.get(position);
        holder.trailer = trailer;
        Picasso.with(mContext).load(NetworkUtils.getThumbnailUrl(trailer.key)).into(holder.thumbnail, new Callback() {
            @Override
            public void onSuccess() {
                holder.playButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {

            }
        });
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

        @BindView(R.id.trailer_thumbnail_image) ImageView thumbnail;
        @BindView(R.id.play_button) ImageButton playButton;

        private final Context context;

        public Trailer trailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();

            playButton.setImageResource(android.R.drawable.ic_media_play);
            playButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(GlobalConstants.DEBUG, "play trailer");
                    if (trailer != null) {
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.key));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + trailer.key));
                        try {
                            context.startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            context.startActivity(webIntent);
                        }
                    }
                }
            });
        }
    }
}
