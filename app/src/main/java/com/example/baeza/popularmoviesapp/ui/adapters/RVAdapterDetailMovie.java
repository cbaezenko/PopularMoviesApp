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
 * Created by baeza on 21.02.2018.
 */

public class RVAdapterDetailMovie extends RecyclerView.Adapter<RVAdapterDetailMovie.RecyclerViewHolder> {

    final private ListItemClickListener mOnClickListener;
    Context context;
    MovieTrailer mMovieTrailer;

    public RVAdapterDetailMovie(Context context, ListItemClickListener onClickListener, MovieTrailer movieTrailer) {
        mOnClickListener = onClickListener;
        this.context = context;
        mMovieTrailer = movieTrailer;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_trailer_rv;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv_trailer.setText("" + mMovieTrailer.getResults().get(position).getSize());
    }

    @Override
    public int getItemCount() {
        return mMovieTrailer.getResults().size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView arrow;
        TextView tv_trailer;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_trailer = itemView.findViewById(R.id.tv_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

}
