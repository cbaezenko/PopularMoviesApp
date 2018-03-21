package com.example.baeza.popularmoviesapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baeza.popularmoviesapp.R;

/**
 * Created by baeza on 20.03.2018.
 */

public class RVAdapterReview extends RecyclerView.Adapter<RVAdapterReview.RVReviewViewHolder> {

    Context context;

    public RVAdapterReview(Context context) {
        this.context = context;
    }

    @Override
    public RVReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = R.layout.item_recycler_review;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout, parent, false);
        RVReviewViewHolder viewHolder = new RVReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RVReviewViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class RVReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_author, tv_content;

        public RVReviewViewHolder(View itemView) {
            super(itemView);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_content = itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
