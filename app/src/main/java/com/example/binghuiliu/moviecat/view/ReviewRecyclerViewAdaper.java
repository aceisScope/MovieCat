package com.example.binghuiliu.moviecat.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binghuiliu.moviecat.R;
import com.example.binghuiliu.moviecat.model.Movie;
import com.example.binghuiliu.moviecat.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by binghuiliu on 18/08/2017.
 */

public class ReviewRecyclerViewAdaper extends RecyclerView.Adapter<ReviewRecyclerViewAdaper.ReviewViewHolder> {

    public ArrayList<Review> reviewData;

    private final LayoutInflater mLayoutInflater;

    private final Context mContext;

    public ReviewRecyclerViewAdaper(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.review_recyclerview_item, parent, false);
        ReviewRecyclerViewAdaper.ReviewViewHolder viewHolder = new ReviewRecyclerViewAdaper.ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviewData.get(position);

        holder.authorTextView.setText(review.author);
        holder.contentTextView.setText(review.content);
        holder.urlTextView.setText(review.url);
    }

    @Override
    public int getItemCount() {
        if (reviewData == null) return 0;
        return reviewData.size();
    }

    public void setReviewData(ArrayList<Review> data) {
        this.reviewData = data;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public @BindView(R.id.text_author) TextView authorTextView;
        public @BindView(R.id.text_content) TextView contentTextView;
        public @BindView(R.id.text_url) TextView urlTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
