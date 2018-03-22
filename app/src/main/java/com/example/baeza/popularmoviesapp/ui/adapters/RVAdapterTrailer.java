package com.example.baeza.popularmoviesapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieTrailer.MovieTrailer;

/**
 * Created by baeza on 20.03.2018.
 */

public class RVAdapterTrailer extends RecyclerView.Adapter<RVAdapterTrailer.RVTrailerViewHolder> {

    Context context;
    MovieTrailer mMovieTrailer;
    final private ListItemClickListener mListItemClickListener;

    public RVAdapterTrailer(Context context, MovieTrailer movieTrailer, ListItemClickListener listItemClickListener) {
        this.context = context;
        mMovieTrailer = movieTrailer;
        mListItemClickListener = listItemClickListener;
    }

    @Override
    public RVTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_trailer_rv;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layout, parent, false);
        RVTrailerViewHolder rvAdapterTrailer = new RVTrailerViewHolder(view);

        return rvAdapterTrailer;
    }

    @Override
    public void onBindViewHolder(RVTrailerViewHolder holder, int position) {
        if (mMovieTrailer != null) {
            holder.tv_trailer.setText(mMovieTrailer.getResults().get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        if (mMovieTrailer != null) return mMovieTrailer.getResults().size();
        else return 1;
    }

    public class RVTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView tv_trailer;

        public RVTrailerViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            tv_trailer = itemView.findViewById(R.id.tv_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
