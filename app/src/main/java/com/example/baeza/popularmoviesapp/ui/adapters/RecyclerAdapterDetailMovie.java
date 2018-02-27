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
 * Created by baeza on 21.02.2018.
 */

public class RecyclerAdapterDetailMovie extends RecyclerView.Adapter<RecyclerAdapterDetailMovie.RecyclerViewHolder> {

    final private  ListItemClickListener mOnClickListener;
    Context context;

    public RecyclerAdapterDetailMovie(Context context,ListItemClickListener onClickListener) {
        mOnClickListener = onClickListener;
        this.context = context;
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
        holder.tv_trailer.setText(context.getString(R.string.trailer_str)+position);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

public interface ListItemClickListener{
    void onListItemClick(int clickedItemIndex);
}

}
