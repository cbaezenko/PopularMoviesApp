package com.example.baeza.popularmoviesapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
    private static final String STATE = "state";

    public static final int POPULAR = 1, TOP_RATED = 2, FAVORITE = 3;

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

        initUIComponents();
        createRecyclerView();

        if (savedInstanceState == null) {

            int selected_preference = read_state_sharedPreference();
            switch (selected_preference) {
                case POPULAR: {
                    try {
                        getRetrofitAnswer(POPULAR);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case TOP_RATED: {
                    try {
                        getRetrofitAnswer(TOP_RATED);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case FAVORITE: {
                    Cursor cursor = getAllMoviesFromContent();
                    populateUIRecyclerViewDataBase(cursor);
                    break;
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getInt(STATE) == POPULAR ||
                savedInstanceState.getInt(STATE) == TOP_RATED) {
            if (savedInstanceState.getParcelable(INFO_TO_KEEP) != null) {
                mMovieRequest = savedInstanceState.getParcelable(INFO_TO_KEEP);
                mRVAdapterMainScreen.addMovieRequestData(mMovieRequest.getResults());
                mRVAdapterMainScreen.notifyDataSetChanged();
            } else {
                showErrorMsg(true);
            }
        } else if (savedInstanceState.getInt(STATE) == FAVORITE) {
            Cursor cursor = getAllMoviesFromContent();
            populateUIRecyclerViewDataBase(cursor);
        }
    }


    private void createRecyclerView() {

        //giving to the recycler view the grid appearance
        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, numberOfColumns());
        mRVAdapterMainScreen = new RVAdapterMainScreen(MainActivity.this, this);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRVAdapterMainScreen);

        scrollListener = new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadPage++;
                loadData(loadPage, read_state_sharedPreference());
            }
        };

        mRecyclerView.addOnScrollListener(scrollListener);
    }

    private int numberOfColumns(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void loadData(int loadPage, int selected) {
        this.loadPage = loadPage;

        switch (selected) {
            case POPULAR: {
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
                                showErrorMsg(true);
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
                break;
            }
            case TOP_RATED: {
                ApiUtils.getApiService().getMovieListTopRatedPage(
                        BuildConfig.KeyForMovies, loadPage)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<MovieRequest>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                showProgressBar(false);
                                showErrorMsg(true);
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
                break;
            }
        }

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
                save_state_sharedPreference(POPULAR);
//                selected = POPULAR;
                mRVAdapterMainScreen.clearMovieRequestData();
                showToast(getString(R.string.show_by_popularity), this);
                try {
                    getRetrofitAnswer(POPULAR);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.top_rared: {
                save_state_sharedPreference(TOP_RATED);
                mRVAdapterMainScreen.clearMovieRequestData();
                try {
                    getRetrofitAnswer(TOP_RATED);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showToast(getString(R.string.show_by_top_rated), this);
                break;
            }
            case R.id.favorites: {
                save_state_sharedPreference(FAVORITE);
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
                        showErrorMsg(true);
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

    private void showErrorMsg(boolean isShownErrorMsg) {
        if (isShownErrorMsg) {
            tv_error_msg.setVisibility(View.VISIBLE);
        } else {
            tv_error_msg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(INFO_TO_KEEP, mMovieRequest);
        outState.putInt(STATE, read_state_sharedPreference());
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
                                showErrorMsg(true);
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
                                showErrorMsg(true);
                            }

                            @Override
                            public void onNext(MovieRequest movieRequest) {
                                tv_error_msg.setVisibility(View.INVISIBLE);
                                showProgressBar(false);

                                mMovieRequest = movieRequest;
                                mRVAdapterMainScreen.addMovieRequestData(movieRequest.getResults());
                                mRVAdapterMainScreen.notifyDataSetChanged();
                                mRecyclerView.setAdapter(mRVAdapterMainScreen);
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

        if (!isNetworkConnection()) {
            showProgressBar(false);
            tv_error_msg.setText(getString(R.string.no_network_connection));
        }

        new InternetCheck(this).execute();
    }

    private void initUIComponents() {
        mRecyclerView = findViewById(R.id.recyclerView_movies);
        progressBar = findViewById(R.id.progressBar);
        tv_error_msg = findViewById(R.id.tv_error_msg);
    }

    private void save_state_sharedPreference(int state) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.preference_movies), state);
        editor.commit();
    }

    private int read_state_sharedPreference() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        int preference_movie = sharedPreferences.getInt(getString(R.string.preference_movies),
                POPULAR);
        return preference_movie;
    }

}