package com.example.baeza.popularmoviesapp;

import android.content.Context;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.model.Movie;
import com.example.baeza.popularmoviesapp.model.RecyclerAdapter;
import com.example.baeza.popularmoviesapp.utilities.JsonUtilities;
import com.example.baeza.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.ListItemClickListener {

    public String TAG = this.getClass().getSimpleName();

    RecyclerView mRecyclerView;
    RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView_movies);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerAdapter = new RecyclerAdapter(MainActivity.this,MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        URL videoUrl = NetworkUtils.buildUrl("popular", getString(R.string.key_movies));
        new FetchMovies().execute(videoUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
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

    @Override
    public void onListItemClick(int clickedItemIndex) {
    }


    public class FetchMovies extends AsyncTask<URL, Void, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonMoviesResponse = null;
            try{
                jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(url);
                JsonUtilities.parseMoviesListJSON(jsonMoviesResponse);

                Log.d(TAG, "json is: " + jsonMoviesResponse.toString());
                return jsonMoviesResponse;
            }
            catch (Exception e){
                Log.d(TAG, "EXCEPTION HERE");
                e.printStackTrace();
                return  null;
            }
        }
        @Override
        protected void onPostExecute(String moviesData){
            if(moviesData != null && !moviesData.equals("")) {

                Log.d(TAG, "movies data " + moviesData);
            } else {
            }
        }
    }

}
