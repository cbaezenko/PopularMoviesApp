package com.example.baeza.popularmoviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private static final String INFO_TO_KEEP =  "info";
    public static final int POPULAR = 1, TOP_RATED = 2, DETAIL_MOVIE = 3;
//    public String TAG = this.getClass().getSimpleName();
    ProgressBar progressBar;

    private Movie movieDetail;

    RecyclerView mRecyclerView;
    RecyclerAdapterMainScreen mRecyclerAdapterMainScreen;
    private static List<Movie> mMovieList = new ArrayList<>();
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState ==null || !savedInstanceState.containsKey(INFO_TO_KEEP)){
        URL videoUrl = NetworkUtils.buildUrl(POPULAR, getString(R.string.key_movies));
        new FetchMovies(MainActivity.POPULAR).execute(videoUrl);
        }
        else {
            mMovieList = savedInstanceState.getParcelableArrayList(INFO_TO_KEEP);
            populateUIwithRecyclerView();
        }
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
                    getString(R.string.show_by_popularity), Toast.LENGTH_SHORT )
                    .show();
                URL videoUrl = NetworkUtils.buildUrl(POPULAR, getString(R.string.key_movies));
                new FetchMovies(MainActivity.POPULAR).execute(videoUrl);
                    break;

            }
            case R.id.top_rared:{
                Toast.makeText(MainActivity.this,
                        getString(R.string.show_by_top_rated), Toast.LENGTH_SHORT )
                        .show();
                URL videoUrl = NetworkUtils.buildUrl(TOP_RATED, getString(R.string.key_movies));
                new FetchMovies(MainActivity.TOP_RATED).execute(videoUrl);
                        break;

            }
            default:{}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
//        Toast.makeText(this, "Pressed "+clickedItemIndex+" id is: "+(mMovieList.get(clickedItemIndex)).getId(), Toast.LENGTH_SHORT).show();
        URL movieDetailURL = NetworkUtils.buildUrl(DETAIL_MOVIE, getString(R.string.key_movies),(mMovieList.get(clickedItemIndex)).getId());
        new FetchMovies(DETAIL_MOVIE).execute(movieDetailURL);
    }

    public class FetchMovies extends AsyncTask<URL, Void, String>{
        int petition;

        FetchMovies(int petition){this.petition = petition;}

        @Override
        protected void onPreExecute(){super.onPreExecute();

        if(mMovieList!=null) {
            if (mMovieList.size() != 0) {
                mMovieList.clear();
            }
        }
        showProgressBar(true);}

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
                startMovieDetailActivity(movieDetail);
                return;
            }
            if(moviesData != null && !moviesData.equals("")) {
//                Log.d(TAG, "movies data " + moviesData);
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
        progressBar = findViewById(R.id.progressBar);
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

    private void startMovieDetailActivity(Movie movieDetail){
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.TITLE_KEY, movieDetail.getTitle());
        intent.putExtra(MovieDetailActivity.POSTER_PATH, movieDetail.getPoster_path());
        intent.putExtra(MovieDetailActivity.OVERVIEW, movieDetail.getOverview());
        intent.putExtra(MovieDetailActivity.RUNTIME, movieDetail.getRuntime());
        intent.putExtra(MovieDetailActivity.VOTE_AVERAGE, movieDetail.getVote_average());
        intent.putExtra(MovieDetailActivity.RELEASE_DATE, movieDetail.getRelease_date());
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList(INFO_TO_KEEP, (ArrayList<? extends Parcelable>) mMovieList);
        super.onSaveInstanceState(outState);
    }
}