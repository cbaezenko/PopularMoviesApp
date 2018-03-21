package com.example.baeza.popularmoviesapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baeza.popularmoviesapp.R;

/**
 * Created by baeza on 20.03.2018.
 */

public class RVAdapterTrailer extends RecyclerView.Adapter<RVAdapterTrailer.RVTrailerViewHolder> {

    Context context;

    public RVAdapterTrailer(Context context) {
    this.context = context;
    }

    @Override
    public RVTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_trailer_rv;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layout, parent, false);
        RVTrailerViewHolder rvAdapterTrailer = new RVTrailerViewHolder(view);

        return  rvAdapterTrailer;
    }

    @Override
    public void onBindViewHolder(RVTrailerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class RVTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView tv_trailer;

        public RVTrailerViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            tv_trailer = itemView.findViewById(R.id.tv_trailer);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
