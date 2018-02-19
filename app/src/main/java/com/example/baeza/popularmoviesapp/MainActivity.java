package com.example.baeza.popularmoviesapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

//    Movie movie;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemThatWasClickedId = item.getItemId();
        switch (item.getItemId()){
            case R.id.popularity:{
            Toast.makeText(MainActivity.this,
                    "Popularity", Toast.LENGTH_SHORT )
                    .show();
                    break;}
            case R.id.top_rared:{
                Toast.makeText(MainActivity.this,
                        "TOP RATED", Toast.LENGTH_SHORT )
                        .show();
                        break;}
            default:{}
        }
        return super.onOptionsItemSelected(item);
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
        @Override public int getCount() {return 8;}
        @Override public Object getItem(int position) {return null;}
        @Override public long getItemId(int position) {return 0;}

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ImageView imageView;
            if(view == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
            else {imageView = (ImageView) view;}

            Picasso.with(MainActivity.this)
//                    .load(movie.getPoster_path())
                    .load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
                    .into(imageView);


            //imageView.setImageResource(mMoviesImages[position]);
            imageView.setAdjustViewBounds(true);
            return imageView;
        }
    }
}
