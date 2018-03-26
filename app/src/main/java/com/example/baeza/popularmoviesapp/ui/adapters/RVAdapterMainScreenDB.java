package com.example.baeza.popularmoviesapp.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by baeza on 28.02.2018.
 */

public class RVAdapterMainScreenDB extends RecyclerView.Adapter<RVAdapterMainScreenDB.RecyclerViewHolder> {

    final private RVAdapterMainScreenDB.ListItemClickListenerContentProvider mOnClickListener;
    private Context context;
    private Cursor mCursor;

    public RVAdapterMainScreenDB(Context context, ListItemClickListenerContentProvider listener, Cursor cursor) {
        mOnClickListener = listener;
        this.context = context;
        mCursor = cursor;
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
        mCursor.moveToPosition(position);

//        cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID));

        String moviePath =
                mCursor.getString(mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID));

        Picasso.with(context)
                .load(moviePath)
                .into(holder.imageMovie);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
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
            mOnClickListener.onListItemClickContentProvider(clickedPosition);
        }
    }

    public interface ListItemClickListenerContentProvider {
        void onListItemClickContentProvider(int clickedItemIndex);
    }
}
