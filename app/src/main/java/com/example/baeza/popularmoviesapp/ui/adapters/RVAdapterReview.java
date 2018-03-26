package com.example.baeza.popularmoviesapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieReview.MovieReview;

/**
 * Created by baeza on 20.03.2018.
 */

public class RVAdapterReview extends RecyclerView.Adapter<RVAdapterReview.RVReviewViewHolder> {

    private Context context;
    private MovieReview mMovieReview;

    public RVAdapterReview(Context context, MovieReview movieReview) {
        this.context = context;
        mMovieReview = movieReview;
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

        if (mMovieReview != null) {
            holder.tv_author.setText(mMovieReview.getResults().get(position).getAuthor());
            holder.tv_content.setText(mMovieReview.getResults().get(position).getContent());
        }
        if (mMovieReview == null) {
            holder.tv_content.setText(context.getString(R.string.no_reviews));
        }
    }

    @Override
    public int getItemCount() {
        if (mMovieReview != null)
            return mMovieReview.getResults().size();
        else
            return 1;
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
            Toast.makeText(view.getContext(), ""+view.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}
