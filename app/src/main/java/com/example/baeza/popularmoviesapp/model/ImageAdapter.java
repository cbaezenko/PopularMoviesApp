package com.example.baeza.popularmoviesapp.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by baeza on 20.02.2018.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movie> movieList;
    public ImageAdapter(Context context, List<Movie> movieList){mContext = context; this.movieList = movieList;}
    @Override public int getCount() {return movieList.size();}
    @Override public Object getItem(int i) {return null;}
    @Override public long getItemId(int i) {return 0;}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if(view == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
        } else{
            imageView =(ImageView) view;
        }
        String path = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";

        Picasso.with(mContext).load(path).into(imageView);
        imageView.setAdjustViewBounds(true);
        return imageView;}
}
