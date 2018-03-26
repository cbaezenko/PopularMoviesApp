package com.example.baeza.popularmoviesapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.baeza.popularmoviesapp.BuildConfig;
import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieDBHelper;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieList.Result;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterMainScreen;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieDetail.MovieDetailRequest;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieList.MovieRequest;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterMainScreenDB;
import com.example.baeza.popularmoviesapp.ui.helper.EndlessRecyclerViewScrollListener;

import com.example.baeza.popularmoviesapp.ui.helper.InternetCheck;

import java.io.IOException;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RVAdapterMainScreen.ListItemClickListener, RVAdapterMainScreenDB.ListItemClickListenerContentProvider {

    private final static String TAG = "MainActivity";
    private static final String INFO_TO_KEEP = "info";
    public static final int POPULAR = 1, TOP_RATED = 2;

    private MovieRequest mMovieRequest;
    private MovieDetailRequest mMovieDetailRequest;

    private EndlessRecyclerViewScrollListener scrollListener;

    public static TextView tv_error_msg;

    static ProgressBar progressBar;

    private RecyclerView mRecyclerView;
    private RVAdapterMainScreen mRVAdapterMainScreen;

    private int loadPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FavoriteMovieDBHelper dbHelper = new FavoriteMovieDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        createRecyclerView();

        progressBar = findViewById(R.id.progressBar);


        if (savedInstanceState == null || !savedInstanceState.containsKey(INFO_TO_KEEP)) {
            try {
                getRetrofitAnswer(POPULAR);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            mMovieRequest = savedInstanceState.getParcelable(INFO_TO_KEEP);
            mRVAdapterMainScreen.addMovieRequestData(mMovieRequest.getResults());
            mRVAdapterMainScreen.notifyDataSetChanged();
        }
    }

    private void createRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView_movies);

        //giving to the recycler view the grid appearance
        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 2);
        mRVAdapterMainScreen = new RVAdapterMainScreen(MainActivity.this, this);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRVAdapterMainScreen);

        scrollListener = new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadPage++;
                loadData(loadPage);
            }
        };

        mRecyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void loadData(int loadPage) {
        this.loadPage = loadPage;

        ApiUtils.getApiService().getMovieListPopularityPage(
                BuildConfig.KeyForMovies, loadPage)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieRequest>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgressBar(false);
                        showErrorMsg();
                    }

                    @Override
                    public void onNext(MovieRequest movieRequest) {
                        tv_error_msg.setVisibility(View.INVISIBLE);
                        showProgressBar(false);

                        mMovieRequest = movieRequest;

                        mRVAdapterMainScreen.addMovieRequestData(movieRequest.getResults());
                        mRVAdapterMainScreen.notifyDataSetChanged();

                        if (scrollListener != null) {
                            scrollListener.resetState();
                        }
                    }
                });

    }

    private void populateUIRecyclerViewDataBase(Cursor cursor) {
        if (mRecyclerView != null) {
            mRecyclerView.removeAllViews();
        }
        RVAdapterMainScreenDB RVAdapterMainScreenDB = new RVAdapterMainScreenDB(MainActivity.this, MainActivity.this, cursor);
        mRecyclerView.setAdapter(RVAdapterMainScreenDB);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadPage = 1;
        switch (item.getItemId()) {
            case R.id.popularity: {
                showToast(getString(R.string.show_by_popularity), this);
                try {
                    getRetrofitAnswer(POPULAR);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.top_rared: {
                try {
                    getRetrofitAnswer(TOP_RATED);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showToast(getString(R.string.show_by_top_rated), this);
                break;
            }
            case R.id.favorites: {
                try {
                    Cursor cursor = getAllMoviesFromContent();
                    if (mRecyclerView != null) {
                        mRecyclerView.removeAllViews();
                    }
                    populateUIRecyclerViewDataBase(cursor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            default: {
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void readFromContent(int clickedItem) {
        Cursor cursor = getAllMoviesFromContent();
        if (cursor.getCount() != 0) {
            cursor.moveToPosition(clickedItem);
            cursor.getCount();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        showProgressBar(true);

        List<Result> resultList = mRVAdapterMainScreen.getResults();

        ApiUtils.getApiService().getMovieDetail(resultList.get(clickedItemIndex).getId(),
                BuildConfig.KeyForMovies)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetailRequest>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgressBar(false);
                        showErrorMsg();
                    }

                    @Override
                    public void onNext(MovieDetailRequest movieDetailRequest) {
                        showProgressBar(false);
                        mMovieDetailRequest = movieDetailRequest;
                        mRVAdapterMainScreen.notifyDataSetChanged();
                        intentToDetailMovieActivity(mMovieDetailRequest);
                    }
                });
    }

    public static void showProgressBar(boolean isShownProgressBar) {
        if (isShownProgressBar) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorMsg() {
        if (true) {
            tv_error_msg.setVisibility(View.VISIBLE);
        } else {
            tv_error_msg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(INFO_TO_KEEP, mMovieRequest);
        super.onSaveInstanceState(outState);
    }

    private void getRetrofitAnswer(int requestID) throws IOException {

        switch (requestID) {
            case POPULAR: {
                if (mMovieRequest != null) {
                    mMovieRequest = null;
                }
                showProgressBar(true);
                ApiUtils.getApiService().getMovieListPopularity(
                        BuildConfig.KeyForMovies)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<MovieRequest>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                showProgressBar(false);
                                showErrorMsg();
                            }

                            @Override
                            public void onNext(MovieRequest movieRequest) {
                                tv_error_msg.setVisibility(View.INVISIBLE);
                                showProgressBar(false);

                                mMovieRequest = movieRequest;
                                mRVAdapterMainScreen.addMovieRequestData(movieRequest.getResults());
                                mRecyclerView.setAdapter(mRVAdapterMainScreen);
                                mRVAdapterMainScreen.notifyDataSetChanged();
                            }
                        });
                break;
            }

            case TOP_RATED: {
                showProgressBar(true);
                if (mMovieRequest != null) {
                    mMovieRequest = null;
                }
                ApiUtils.getApiService().getMovieListRated(
                        BuildConfig.KeyForMovies)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<MovieRequest>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                showProgressBar(false);
                                showErrorMsg();
                            }

                            @Override
                            public void onNext(MovieRequest movieRequest) {
                                tv_error_msg.setVisibility(View.INVISIBLE);
                                showProgressBar(false);

                                mMovieRequest = movieRequest;
                                mRVAdapterMainScreen.addMovieRequestData(movieRequest.getResults());
                                mRecyclerView.setAdapter(mRVAdapterMainScreen);
                                mRVAdapterMainScreen.notifyDataSetChanged();
                            }
                        });
                break;
            }
            default: {
                //do nothing
            }
        }
    }

    private Cursor getAllMoviesFromContent() {
        try {
            tv_error_msg.setVisibility(View.INVISIBLE);

            return getContentResolver().query(FavoriteMovieContract.FavoriteMovie.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    private void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemClickContentProvider(int clickedItemIndex) {
        readFromContent(clickedItemIndex);
        Cursor cursor = getCursorFromClick();
        cursor.moveToPosition(clickedItemIndex);
        showProgressBar(true);
        intentMovieActivityFavorites(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID)),
                cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_OVERVIEW)),
                cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_RUNTIME)),
                cursor.getDouble(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_USER_RATING)),
                cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_RELEASE_DATE)),
                cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_TITLE)),
                cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID)),
                cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_BACKDROP_IMAGE))
        );
    }

    //for test the custom detailed movie page
    private void intentToDetailMovieActivity(MovieDetailRequest movieDetailRequest) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.POSTER_PATH, ApiUtils.getUrlBaseForImageMovie() + movieDetailRequest.getPosterPath());
        intent.putExtra(MovieDetailActivity.OVERVIEW, movieDetailRequest.getOverview());
        intent.putExtra(MovieDetailActivity.RUNTIME, movieDetailRequest.getRuntime());
        intent.putExtra(MovieDetailActivity.VOTE_AVERAGE, movieDetailRequest.getVoteAverage());
        intent.putExtra(MovieDetailActivity.RELEASE_DATE, movieDetailRequest.getReleaseDate());
        intent.putExtra(MovieDetailActivity.TITLE_KEY, movieDetailRequest.getTitle());
        intent.putExtra(MovieDetailActivity.ID_MOVIE, movieDetailRequest.getId());
        intent.putExtra(MovieDetailActivity.BACKDROP_PATH, ApiUtils.getUrlBackdropImage() + movieDetailRequest.getBackdropPath());
        startActivity(intent);
    }

    private void intentMovieActivityFavorites(String poster_path, String overview, int runtime, double voteAverage, String release_date,
                                              String title, int id_movie, String backdrop_path) {

        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.POSTER_PATH, poster_path);
        intent.putExtra(MovieDetailActivity.OVERVIEW, overview);
        intent.putExtra(MovieDetailActivity.RUNTIME, runtime);
        intent.putExtra(MovieDetailActivity.VOTE_AVERAGE, voteAverage);
        intent.putExtra(MovieDetailActivity.RELEASE_DATE, release_date);
        intent.putExtra(MovieDetailActivity.TITLE_KEY, title);
        intent.putExtra(MovieDetailActivity.ID_MOVIE, id_movie);
        intent.putExtra(MovieDetailActivity.BACKDROP_PATH, backdrop_path);
        startActivity(intent);
    }


    private Cursor getCursorFromClick() {
        try {
            return getContentResolver().query(FavoriteMovieContract.FavoriteMovie.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isNetworkConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_error_msg = findViewById(R.id.tv_error_msg);

        if (!isNetworkConnection()) {
            showProgressBar(false);
            tv_error_msg.setText(getString(R.string.no_network_connection));
        }

        new InternetCheck(this).execute();
    }
}