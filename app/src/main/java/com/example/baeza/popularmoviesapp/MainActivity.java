package com.example.baeza.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

        showGridWithContent();
         /*
        Al iniciar la app, mostrar un loader mientras se ejecuta la peticion
         */
        //        makeVIDEOQuery();
    }

    private void makeVIDEOQuery(){
        URL videoUrl = NetworkUtils.buildUrl("popular", getString(R.string.key_movies));
    }

    private void showGridWithContent(){
        GridView gridView = findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(MainActivity.this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ImageAdapter extends BaseAdapter{
        private Context mContext;
        public ImageAdapter(Context context){mContext = context;}
        @Override public int getCount() {return mMoviesImages.length;}
        @Override public Object getItem(int position) {return null;}
        @Override public long getItemId(int position) {return 0;}

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ImageView imageView;
            if(view == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(-2,
                        dpPixelsHeight()));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {imageView = (ImageView) view;}
            imageView.setImageResource(mMoviesImages[position]);
            imageView.setAdjustViewBounds(true);
            return imageView;
        }

        //test image for proving the layout
        private Integer[] mMoviesImages = {
                R.drawable.iplay,R.drawable.iplay,
                R.drawable.iplay,R.drawable.iplay,
                R.drawable.iplay,R.drawable.iplay,
                R.drawable.iplay,R.drawable.iplay,
                R.drawable.iplay,R.drawable.iplay,
                R.drawable.iplay,R.drawable.iplay,
                R.drawable.iplay,R.drawable.iplay};
    }

    private int dpPixelsHeight(){
        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        int dpWidth = (int) (displayMetrics.widthPixels/displayMetrics.density);
        return (int)(dpWidth * 1.3);
    }

}
