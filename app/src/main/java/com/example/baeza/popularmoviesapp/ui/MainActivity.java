package com.example.baeza.popularmoviesapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieDBHelper;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterMainScreen;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieDetail.MovieDetailRequest;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieList.MovieRequest;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterMainScreenDB;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.function.Consumer;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RVAdapterMainScreen.ListItemClickListener, RVAdapterMainScreenDB.ListItemClickListenerContentProvider {

    private final static String TAG = "MainActivity";
    private static final String INFO_TO_KEEP =  "info";
    public static final int POPULAR = 1, TOP_RATED = 2;
    ProgressBar progressBar;
    private SQLiteDatabase mDb;

    private MovieRequest mMovieRequest;
    private MovieDetailRequest mMovieDetailRequest;

    TextView tv_error_msg;

    RecyclerView mRecyclerView;
    RVAdapterMainScreen mRVAdapterMainScreen;
    RVAdapterMainScreenDB mRVAdapterMainScreenDB;

    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FavoriteMovieDBHelper dbHelper = new FavoriteMovieDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        if(savedInstanceState == null || !savedInstanceState.containsKey(INFO_TO_KEEP)){
            try {getRetrofitAnswer(POPULAR);}
            catch (IOException e) {e.printStackTrace();}}
        else {
            mMovieRequest = savedInstanceState.getParcelable(INFO_TO_KEEP);
            populateUIwithRecyclerViewRetro(mMovieRequest);}
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
        mRVAdapterMainScreen = new RVAdapterMainScreen(MainActivity.this, MainActivity.this, movieRequest);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRVAdapterMainScreen);
    }

    private void populateUIRecyclerViewDataBase(Cursor cursor){
        if(mRecyclerView!=null){mRecyclerView.removeAllViews();}
        mRecyclerView = findViewById(R.id.recyclerView_movies);

        //giving to the recycler view the grid appearance
        recyclerViewLayoutManager = new GridLayoutManager(this, 2);
        mRVAdapterMainScreenDB = new RVAdapterMainScreenDB(MainActivity.this, MainActivity.this, cursor);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRVAdapterMainScreenDB);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.popularity:{
                showToast(getString(R.string.show_by_popularity), this);
                try {getRetrofitAnswer(POPULAR);}
                catch (IOException e) {e.printStackTrace();}
                    break;
            }
            case R.id.top_rared:{
                try {getRetrofitAnswer(TOP_RATED);}
                catch (IOException e) {e.printStackTrace();}
                        showToast(getString(R.string.show_by_top_rated), this);
                        break;
            }
            case R.id.favorites:{
                try{
//                    Cursor cursor = getAllMovies();
                    Cursor cursor = getAllMoviesFromContent();

                    mRecyclerView.removeAllViews();
                    populateUIRecyclerViewDataBase(cursor);
                }catch (Exception e){
            e.printStackTrace();
                    Log.d(TAG, "EXCEPTION HERE");
                }
                break;
            }
            default:{}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        showProgressBar(true);

        ApiUtils.getApiService().getMovieDetail(mMovieRequest.getResults().get(clickedItemIndex).getId(),
                getString(R.string.key_movies))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetailRequest>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        showProgressBar(false);
                        showErrorMsg(true);}
                    @Override
                    public void onNext(MovieDetailRequest movieDetailRequest) {
                        showProgressBar(false);
                        mMovieDetailRequest = movieDetailRequest;
                        intentToDetailMovieActivity(mMovieDetailRequest);
                    }});
    }

    private void showProgressBar(boolean isShownProgressBar){
        progressBar = findViewById(R.id.progressBar);
        if(isShownProgressBar){
            progressBar.setVisibility(View.VISIBLE);}
        else{
            progressBar.setVisibility(View.INVISIBLE);}
    }

    private void showErrorMsg(boolean isShownErrorMsg){
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
                ApiUtils.getApiService().getMovieListPopularity(getString(R.string.key_movies))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<MovieRequest>() {
                            @Override public void onCompleted() {}
                            @Override public void onError(Throwable e) {
                                showProgressBar(false);
                                showErrorMsg(true);}
                            @Override public void onNext(MovieRequest movieRequest) {
                                showProgressBar(false);
                                populateUIwithRecyclerViewRetro(movieRequest);
                                mMovieRequest =movieRequest;
                            }});
                break;}

            case TOP_RATED: {
                showProgressBar(true);
                if(mMovieRequest!=null){mMovieRequest=null;}
                ApiUtils.getApiService().getMovieListRated(getString(R.string.key_movies))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<MovieRequest>() {
                            @Override public void onCompleted() {}
                            @Override public void onError(Throwable e) {
                                showProgressBar(false);
                                showErrorMsg(true);}
                            @Override public void onNext(MovieRequest movieRequest) {
                                showProgressBar(false);
                                populateUIwithRecyclerViewRetro(movieRequest);
                                mMovieRequest = movieRequest;
                            }});
                break;}

            default:{
                //do nothing
            }
        }}

        //Hacerlo en un RX
//        private Cursor getAllMovies(){
//            return mDb.query(
//                    FavoriteMovieContract.FavoriteMovie.TABLE_NAME,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null);
//        }


    //Hacerlo en un RX
    private Cursor getAllMoviesFromContent(){
            try {
                return getContentResolver().query(FavoriteMovieContract.FavoriteMovie.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }catch (Exception e){
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;}
    }

    private void showToast(String text, Context context){Toast.makeText(context, text, Toast.LENGTH_SHORT).show();}

    @Override
    public void onListItemClickContentProvider(int clickedItemIndex) {
            Cursor cursor = getCursorFromClick();
            cursor.moveToPosition(clickedItemIndex);
            int id = cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID));
                showProgressBar(true);
                ApiUtils.getApiService().getMovieDetail(id, getString(R.string.key_movies))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetailRequest>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                    showProgressBar(false);
                    showErrorMsg(true);}
                    @Override
                    public void onNext(MovieDetailRequest movieDetailRequest) {
                        showProgressBar(false);
                        mMovieDetailRequest = movieDetailRequest;
                        intentToDetailMovieActivity(mMovieDetailRequest);
                    }});
    }

    private void intentToDetailMovieActivity(MovieDetailRequest movieDetailRequest){
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.POSTER_PATH, ApiUtils.getUrlBaseForImageMovie() + movieDetailRequest.getPosterPath());
        intent.putExtra(MovieDetailActivity.OVERVIEW, movieDetailRequest.getOverview());
        intent.putExtra(MovieDetailActivity.RUNTIME, movieDetailRequest.getRuntime());
        intent.putExtra(MovieDetailActivity.VOTE_AVERAGE, movieDetailRequest.getVoteAverage());
        intent.putExtra(MovieDetailActivity.RELEASE_DATE, movieDetailRequest.getReleaseDate());
        intent.putExtra(MovieDetailActivity.TITLE_KEY, movieDetailRequest.getTitle());
        intent.putExtra(MovieDetailActivity.ID_MOVIE, movieDetailRequest.getId());
        startActivity(intent);
    }

    private Cursor getCursorFromClick(){
        try{
            return getContentResolver().query(FavoriteMovieContract.FavoriteMovie.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);}
        catch (Exception e){
            e.printStackTrace();
            return  null;}
    }



    class InternetCheck extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                int timeoutMs = 1500;
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8",53);

                socket.connect(socketAddress, timeoutMs);
                socket.close();

                return  true;
            }
            catch (IOException e){return false;}
        }

        @Override
        protected void onPostExecute(Boolean isConnected){
            if(!isConnected){
                tv_error_msg.setText(getString(R.string.no_internet_connection));
            }
        }
    }

    private boolean isNetworkConnection(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onResume(){
        super.onResume();
        tv_error_msg = findViewById(R.id.tv_error_msg);

        if(!isNetworkConnection()){
             tv_error_msg.setText(getString(R.string.no_network_connection));
         }

        new InternetCheck();

    }

//        private void onClickAddFavMovie(View view){
//           // addNewFavoriteMovie()
//        }
//
//        private long addNewFavoriteMovie(int id, String poster_path){
//            ContentValues cv = new ContentValues();
//            cv.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID, id);
//            cv.put(FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID,poster_path);
//            return mDb.insert(FavoriteMovieContract.FavoriteMovie.TABLE_NAME, null, cv);
//        }
}