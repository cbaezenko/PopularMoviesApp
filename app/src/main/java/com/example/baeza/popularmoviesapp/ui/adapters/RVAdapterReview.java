package com.example.baeza.popularmoviesapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by baeza on 20.03.2018.
 */

public class RVAdapterReview extends RecyclerView.Adapter<RVAdapterReview.RVReviewViewHolder> {

    public RVAdapterReview() {
    }

    @Override
    public RVReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RVReviewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RVReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RVReviewViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
