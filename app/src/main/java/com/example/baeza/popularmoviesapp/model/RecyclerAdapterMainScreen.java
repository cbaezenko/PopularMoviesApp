package com.example.baeza.popularmoviesapp.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by baeza on 19.02.2018.
 */

public class RecyclerAdapterMainScreen extends  RecyclerView.Adapter<RecyclerAdapterMainScreen.RecyclerViewHolder>{

    final private ListItemClickListener mOnClickListener;
    Context context;
    List<Movie> movieList;

    public RecyclerAdapterMainScreen(Context context, ListItemClickListener listener, List<Movie> movieList){
        mOnClickListener = listener;
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_recycler_view;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout, parent,false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //String path = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        String moviePath = NetworkUtils.getUrlBaseForImageMovie()+(movieList.get(position)).getPoster_path();
        Picasso.with(context)
                .load(moviePath)
                .into(holder.imageMovie);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
    public interface  ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }
}
