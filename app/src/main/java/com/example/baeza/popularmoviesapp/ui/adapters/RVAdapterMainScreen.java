package com.example.baeza.popularmoviesapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieList.MovieRequest;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by baeza on 19.02.2018.
 */

public class RVAdapterMainScreen extends RecyclerView.Adapter<RVAdapterMainScreen.RecyclerViewHolder> {

    final private ListItemClickListener mOnClickListener;
    Context context;
    MovieRequest mMovieRequest;

    public RVAdapterMainScreen(Context context, ListItemClickListener listener, MovieRequest movieRequest) {
        mOnClickListener = listener;
        this.context = context;
        mMovieRequest = movieRequest;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_recycler_view;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //String path = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        String moviePath = ApiUtils.getUrlBaseForImageMovie() + (
                mMovieRequest.getResults().get(position).getPosterPath());

        Picasso.with(context)
                .load(moviePath)
                .into(holder.imageMovie);
    }

    @Override
    public int getItemCount() {
        return mMovieRequest.getResults().size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageMovie;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imageMovie = itemView.findViewById(R.id.leftMovie);
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
