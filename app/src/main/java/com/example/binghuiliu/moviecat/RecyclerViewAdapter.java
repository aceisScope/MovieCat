package com.example.binghuiliu.moviecat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by binghuiliu on 27/07/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieViewHolder>{

    String[] mData = null;

    public RecyclerViewAdapter(String[] data) {
        this.mData = data;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.myTextView.setText(Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView myTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.item_text);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
