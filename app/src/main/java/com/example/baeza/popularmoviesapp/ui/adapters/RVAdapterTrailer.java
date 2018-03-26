package com.example.baeza.popularmoviesapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieTrailer.MovieTrailer;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;

/**
 * Created by baeza on 20.03.2018.
 */

public class RVAdapterTrailer extends RecyclerView.Adapter<RVAdapterTrailer.RVTrailerViewHolder> {

    private Context context;
    private MovieTrailer mMovieTrailer;
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
        public ImageButton buttonPlay;
        public TextView tv_trailer;
        public ImageButton shareButton;

        public RVTrailerViewHolder(View itemView) {
            super(itemView);

            buttonPlay = itemView.findViewById(R.id.buttonPlay);
            buttonPlay.setOnClickListener(this);
            tv_trailer = itemView.findViewById(R.id.tv_trailer);
            shareButton = itemView.findViewById(R.id.shareButton);
            shareButton.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();

            if (view.getId() == R.id.shareButton) {
                String trailer = ApiUtils.getBaseYoutubeVideos() + mMovieTrailer.getResults().get(clickedPosition).getKey();
                shareTrailer(trailer);

            } else if (view.getId() == R.id.buttonPlay) {
                mListItemClickListener.onListItemClick(clickedPosition);
            }
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    private void shareTrailer(String trailer) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, trailer);
        intent.setType("text/plain");

        Intent chooser = Intent.createChooser(intent, context.getString(R.string.share_with));
        context.startActivity(chooser);
    }
}
