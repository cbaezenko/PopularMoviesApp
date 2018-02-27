package com.example.baeza.popularmoviesapp;

import android.content.Intent;
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

import com.example.baeza.popularmoviesapp.model.adapters.RecyclerAdapterMainScreen;
import com.example.baeza.popularmoviesapp.model.movieDetail.MovieDetailRequest;
import com.example.baeza.popularmoviesapp.model.movieList.MovieRequest;
import com.example.baeza.popularmoviesapp.utilities.ApiUtils;

import java.io.IOException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RecyclerAdapterMainScreen.ListItemClickListener {

    private final static String TAG = "MainActivity";

    private static final String INFO_TO_KEEP =  "info";
    public static final int POPULAR = 1, TOP_RATED = 2, DETAIL_MOVIE = 3;
    ProgressBar progressBar;

    private MovieRequest mMovieRequest;
    private MovieDetailRequest mMovieDetailRequest;

    RecyclerView mRecyclerView;
    RecyclerAdapterMainScreen mRecyclerAdapterMainScreen;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null || !savedInstanceState.containsKey(INFO_TO_KEEP)){
            try {getRetrofitAnswer(POPULAR);}
            catch (IOException e) {e.printStackTrace();}
        }
        else {
            mMovieRequest = savedInstanceState.getParcelable(INFO_TO_KEEP);
            populateUIwithRecyclerViewRetro(mMovieRequest);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void populateUIwithRecyclerViewRetro(MovieRequest movieRequest){
        if(mRecyclerView!=null){mRecyclerView.removeAllViews();}
        mRecyclerView = findViewById(R.id.recyclerView_movies);

        //giving to the recycler view the grid appearance
        recyclerViewLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerAdapterMainScreen = new RecyclerAdapterMainScreen(MainActivity.this, MainActivity.this, movieRequest);
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
                try {getRetrofitAnswer(POPULAR);}
                catch (IOException e) {e.printStackTrace();}
                    break;

            }
            case R.id.top_rared:{
                try {getRetrofitAnswer(TOP_RATED);}
                catch (IOException e) {e.printStackTrace();}
                Toast.makeText(MainActivity.this,
                        getString(R.string.show_by_top_rated), Toast.LENGTH_SHORT )
                        .show();
                        break;
            }
            default:{}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        ApiUtils.getApiServiceMovieDetail().getMovieDetail(mMovieRequest.getResults().get(clickedItemIndex).getId(),
                getString(R.string.key_movies))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetailRequest>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MovieDetailRequest movieDetailRequest) {

                        mMovieDetailRequest = movieDetailRequest;
                    Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.POSTER_PATH, ApiUtils.getUrlBaseForImageMovie()+mMovieDetailRequest.getPosterPath());
                    intent.putExtra(MovieDetailActivity.OVERVIEW, mMovieDetailRequest.getOverview());
                    intent.putExtra(MovieDetailActivity.RUNTIME, mMovieDetailRequest.getRuntime());
                    intent.putExtra(MovieDetailActivity.VOTE_AVERAGE, mMovieDetailRequest.getVoteAverage());
                    intent.putExtra(MovieDetailActivity.RELEASE_DATE, mMovieDetailRequest.getReleaseDate());
                    intent.putExtra(MovieDetailActivity.TITLE_KEY, movieDetailRequest.getTitle());
                    startActivity(intent);
                    }
                });

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

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putParcelable(INFO_TO_KEEP, mMovieRequest);
        super.onSaveInstanceState(outState);
    }

    private void getRetrofitAnswer(int requestID) throws IOException {
        switch (requestID) {
            case POPULAR: {
                if(mMovieRequest!=null){mMovieRequest=null;}
                showProgressBar(true);
                ApiUtils.getApiServiceMovieList().getMovieListPopularity(getString(R.string.key_movies))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<MovieRequest>() {
                            @Override
                            public void onCompleted() {}
                            @Override
                            public void onError(Throwable e) {
                                showProgressBar(false);
                                showErrorMsg(true);}
                            @Override
                            public void onNext(MovieRequest movieRequest) {
                                showProgressBar(false);
                                populateUIwithRecyclerViewRetro(movieRequest);
                                mMovieRequest =movieRequest;
                            }});
                break;
            }

            case TOP_RATED: {
                showProgressBar(true);
                if(mMovieRequest!=null){mMovieRequest=null;}
                ApiUtils.getApiServiceMovieList().getMovieListRated(getString(R.string.key_movies))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<MovieRequest>() {
                            @Override
                            public void onCompleted() {}
                            @Override
                            public void onError(Throwable e) {
                                showProgressBar(false);
                                showErrorMsg(true);}
                            @Override
                            public void onNext(MovieRequest movieRequest) {
                                showProgressBar(false);
                                populateUIwithRecyclerViewRetro(movieRequest);
                                mMovieRequest = movieRequest;
                            }});
                break;
            }

            default:{
                //do nothing
            }
        }
    }

}