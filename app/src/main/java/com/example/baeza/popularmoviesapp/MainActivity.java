package com.example.baeza.popularmoviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.model.Movie;
import com.example.baeza.popularmoviesapp.model.RecyclerAdapterMainScreen;
import com.example.baeza.popularmoviesapp.utilities.JsonUtilities;
import com.example.baeza.popularmoviesapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerAdapterMainScreen.ListItemClickListener {

    public static final int POPULAR = 1, TOP_RATED = 2, DETAIL_MOVIE = 3;
    public String TAG = this.getClass().getSimpleName();

    private Movie movieDetail;

    RecyclerView mRecyclerView;
    RecyclerAdapterMainScreen mRecyclerAdapterMainScreen;
    private static List<Movie> mMovieList = new ArrayList<>();
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL videoUrl = NetworkUtils.buildUrl(POPULAR, getString(R.string.key_movies));
        new FetchMovies(MainActivity.POPULAR).execute(videoUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void populateUIwithRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView_movies);

        //giving to the recycler view the grid appearance
        recyclerViewLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerAdapterMainScreen = new RecyclerAdapterMainScreen(MainActivity.this, MainActivity.this, mMovieList);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapterMainScreen);
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
        Toast.makeText(this, "Pressed "+clickedItemIndex+" id is: "+(mMovieList.get(clickedItemIndex)).getId(), Toast.LENGTH_SHORT).show();
        URL movieDetailURL = NetworkUtils.buildUrl(DETAIL_MOVIE, getString(R.string.key_movies),(mMovieList.get(clickedItemIndex)).getId());
        new FetchMovies(DETAIL_MOVIE).execute(movieDetailURL);
    }

    public class FetchMovies extends AsyncTask<URL, Void, String>{
        int petition;
        FetchMovies(int petition){this.petition = petition;}

        @Override
        protected void onPreExecute(){super.onPreExecute(); showProgressBar(true);}

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonMoviesResponse = null;
            try{
                jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(url);

                    if(petition != MainActivity.DETAIL_MOVIE){
                    populateMovieList(JsonUtilities.parseMoviesListJSON(jsonMoviesResponse));}

                    else {
                        movieDetail = JsonUtilities.parseDetailMovieJSON(jsonMoviesResponse);
                    }
                return jsonMoviesResponse;
            }
            catch (Exception e){
                e.printStackTrace();
                return  null;
            }
        }
        @Override
        protected void onPostExecute(String moviesData){
            showProgressBar(false);
            if(petition == MainActivity.DETAIL_MOVIE && moviesData != null && !moviesData.equals("")){
                startMovieDetailActivity(movieDetail.getTitle(),
                        movieDetail.getPoster_path(),
                        movieDetail.getOverview(),
                        movieDetail.getRuntime(),
                        movieDetail.getVote_average());
                return;
            }
            if(moviesData != null && !moviesData.equals("")) {
                Log.d(TAG, "movies data " + moviesData);
                populateUIwithRecyclerView();
            } else {
                showErrorMsg(true);
            }
        }
    }
    private void populateMovieList( List<Movie> movieList){
        if(mMovieList.size() != 0){ mMovieList.clear(); }
        else { mMovieList.addAll(movieList); }
    }

    private void showProgressBar(boolean isShownProgressBar){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        if(isShownProgressBar){
            progressBar.setVisibility(View.VISIBLE);}
        else{
            progressBar.setVisibility(View.INVISIBLE);}
    }

    private void showErrorMsg(boolean isShownErrorMsg){
        TextView tv_error_msg = findViewById(R.id.tv_error_msg);
        if(isShownErrorMsg){
            tv_error_msg.setVisibility(View.VISIBLE);}
        else{
            tv_error_msg.setVisibility(View.INVISIBLE);}
    }

    private void startMovieDetailActivity(String title,String poster_path,String overview,int runtime, double vote_average){
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(MovieDetail.TITLE_KEY, title);
        intent.putExtra(MovieDetail.POSTER_PATH, poster_path);
        intent.putExtra(MovieDetail.OVERVIEW, overview);
        intent.putExtra(MovieDetail.RUNTIME, runtime);
        intent.putExtra(MovieDetail.VOTE_AVERAGE, vote_average);
        startActivity(intent);
    }
}