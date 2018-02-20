package com.example.baeza.popularmoviesapp.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.utilities.JsonUtilities;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by baeza on 19.02.2018.
 */

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    final private ListItemClickListener mOnClickListener;
    Context context;
    List<Movie> movieList;
    private static String TAG = RecyclerAdapter.class.getSimpleName();

    public RecyclerAdapter(Context context, ListItemClickListener listener, List<Movie> movieList){
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

//        for(int i=0; i<movieList.size();i++){
//            Log.d(TAG, (movieList.get(i)).getPoster_path());
//            Log.d(TAG, ""+(movieList.get(i)).getId());
//        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //String path = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";

        Log.d(TAG, "position is >>>>> "+position +" position movie list"+movieList.get(position) + "size array "+movieList.size());

        int posLeft = position, posRight = position;
        if(position == 0){
            posLeft = position;
            posRight = position+1;
        }
        else{
            posLeft = posLeft * 2;
            posRight = posLeft + 1;
        }
        String movieLeft = "http://image.tmdb.org/t/p/w185/"+(movieList.get(posLeft)).getPoster_path();
        Log.d(TAG, "left is >>>>> "+movieLeft);

        Picasso.with(context)
                .load(movieLeft)
                .into(holder.leftImage);

//        if(position==0){position=;}
        String movieRight = "http://image.tmdb.org/t/p/w185/"+(movieList.get(posRight)).getPoster_path();

        Picasso.with(context)
                .load(movieRight)
                .into(holder.rightImage);
    }

    @Override
    public int getItemCount() {
        return movieList.size()/2;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView leftImage,rightImage;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            leftImage = itemView.findViewById(R.id.leftMovie);
            rightImage = itemView.findViewById(R.id.rightMovie);
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
