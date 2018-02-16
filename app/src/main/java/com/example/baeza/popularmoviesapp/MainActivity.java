package com.example.baeza.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(MainActivity.this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });
         /*
        Al iniciar la app, mostrar un loader mientras se ejecuta la peticion
         */
        makeVIDEOQuery();
    }

    private void makeVIDEOQuery(){
        URL videoUrl = NetworkUtils.buildUrl("popular", getString(R.string.key_movies));
    }

    public class ImageAdapter extends BaseAdapter{
        private Context mContext;
        public ImageAdapter(Context context){mContext = context;}
        @Override public int getCount() {return mMoviesImages.length;}
        @Override public Object getItem(int position) {return null;}
        @Override public long getItemId(int position) {return 0;}

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ImageView imageView;
            if(view == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT,
                        GridLayout.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {imageView = (ImageView) view;}
            imageView.setImageResource(mMoviesImages[position]);
            return imageView;
        }

        private Integer[] mMoviesImages = {
                R.drawable.iplay,R.drawable.iplay
                ,R.drawable.iplay,R.drawable.iplay,
                R.drawable.iplay,R.drawable.iplay,
                R.drawable.iplay,R.drawable.iplay
                ,R.drawable.iplay,R.drawable.iplay,R.drawable.iplay,R.drawable.iplay
                ,R.drawable.iplay,R.drawable.iplay};
    }

}
