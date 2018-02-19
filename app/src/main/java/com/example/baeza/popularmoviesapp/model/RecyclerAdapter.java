package com.example.baeza.popularmoviesapp.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.utilities.JsonUtilities;
import com.squareup.picasso.Picasso;

/**
 * Created by baeza on 19.02.2018.
 */

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    final private ListItemClickListener mOnClickListener;
    Context context;

    public RecyclerAdapter(Context context,ListItemClickListener listener){
        mOnClickListener=listener;
        this.context = context;
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
        String path = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        Picasso.with(context)
                .load(path.trim())
                .into(holder.leftImage);
       // holder.leftImage.setImageResource(R.drawable.image);


        Picasso.with(context)
                .load(path.trim())
                .into(holder.rightImage);
      //  holder.rightImage.setImageResource(R.drawable.image);

    }

    @Override
    public int getItemCount() {
        return 8;
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
