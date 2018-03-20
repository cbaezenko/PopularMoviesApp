package com.example.baeza.popularmoviesapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by baeza on 20.03.2018.
 */

public class RVAdapterTrailer extends RecyclerView.Adapter<RVAdapterTrailer.RVTrailerViewHolder> {

    public RVAdapterTrailer() {
    }

    @Override
    public RVTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RVTrailerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RVTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RVTrailerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
