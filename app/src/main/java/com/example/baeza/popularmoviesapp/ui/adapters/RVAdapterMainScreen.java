package com.example.baeza.popularmoviesapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieList.MovieRequest;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieList.Result;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by baeza on 19.02.2018.
 */

public class RVAdapterMainScreen extends RecyclerView.Adapter<RVAdapterMainScreen.RecyclerViewHolder> {

    private ArrayList<Result> mResults;
    final private ListItemClickListener mOnClickListener;
    private Context context;

    public RVAdapterMainScreen(Context context, ListItemClickListener listener, MovieRequest movieRequest) {
        mOnClickListener = listener;
        this.context = context;
    }

    public RVAdapterMainScreen(Context context, ListItemClickListener listener) {
        this.mOnClickListener = listener;
        this.context = context;
        mResults = new ArrayList<>();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_recycler_view;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        String moviePath = ApiUtils.getUrlBaseForImageMovie() + (
                mResults.get(position).getPosterPath());

        Picasso.with(context)
                .load(moviePath)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(holder.imageMovie);
    }

    @Override
    public int getItemCount() {

        if (mResults == null) {
            return 0;
        } else
            return mResults.size();
    }

    public void addMovieRequestData(List<Result> resultList) {
        mResults.addAll(resultList);
        notifyItemRangeInserted(getItemCount() + 1, resultList.size());
    }

    public void clearMovieRequestData() {
        mResults.clear();
        notifyDataSetChanged();
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

    public ArrayList<Result> getResults() {
        return mResults;
    }

    public void setResults(ArrayList<Result> results) {
        mResults = results;
    }
}
