package com.example.baeza.popularmoviesapp.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieDBHelper;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieReview.MovieReview;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieTrailer.MovieTrailer;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterDetailMovie;
import com.squareup.picasso.Picasso;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baeza on 16.02.2018.
 */

public class MovieDetailActivity extends AppCompatActivity implements RVAdapterDetailMovie.ListItemClickListener {

    public final static String TITLE_KEY = "title_key";
    public final static String POSTER_PATH = "poster_path_key";
    public final static String OVERVIEW = "overview_key";
    public final static String RUNTIME = "runtime_key";
    public final static String VOTE_AVERAGE = "vote_average";
    public final static String RELEASE_DATE = "release_date";
    public final static String ID_MOVIE = "id";
    private CoordinatorLayout mCoordinatorLayout;

    protected static final String TAG = "MovieDetailActivity";

    private SQLiteDatabase mDb;
    private MovieTrailer mMovieTrailer;
    private MovieReview mMovieReview;

    private RecyclerView mRecyclerView;
    private RVAdapterDetailMovie mRVAdapterDetailMovie;
    private ImageView iv_poster;
    private TextView tvTitle, tvOverview, tvRunTime, tvVoteAverage, tvReleaseDate;
    private String titleMovie, posterPath, overview, runtime, voteAverage, release_date;
    private int id;
    ImageButton btnFavorite;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);

        getExtrasFromIntent();
        initUIItems();

        requestMovieTrailer(id, getString(R.string.key_movies));

        tvTitle.setText(titleMovie);
        tvOverview.setText(overview);
        tvRunTime.setText(runtime+getString(R.string.minutes));
        tvVoteAverage.setText(voteAverage+getString(R.string.vote_average_design));
        tvReleaseDate.setText(getOnlyYear(release_date));
        Log.d(TAG,"poster path "+ posterPath);
        Picasso.with(this).load(posterPath).into(iv_poster);
    }

    private String getOnlyYear(String release_date){
        String yearRelease = release_date.substring(0,4);
        return yearRelease;
    }

    private void populateUIwithRecyclerView(MovieTrailer movieTrailer){
        mRecyclerView = findViewById(R.id.rv_trailers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        mRVAdapterDetailMovie = new RVAdapterDetailMovie(MovieDetailActivity.this, this, movieTrailer);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRVAdapterDetailMovie);
    }

    public void getExtrasFromIntent(){
        titleMovie = getIntent().getStringExtra(TITLE_KEY);
        posterPath = getIntent().getStringExtra(POSTER_PATH);
        overview = getIntent().getStringExtra(OVERVIEW);
        voteAverage = Double.toString(getIntent().getDoubleExtra(VOTE_AVERAGE, 0.0));
        runtime = Integer.toString(getIntent().getIntExtra(RUNTIME, 0));
        release_date = getIntent().getStringExtra(RELEASE_DATE);
        id = getIntent().getIntExtra(ID_MOVIE,1);
    }

    private void initUIItems(){
        tvTitle = findViewById(R.id.tv_title);
        iv_poster = findViewById(R.id.iv_poster);
        tvOverview = findViewById(R.id.overview);
        tvRunTime = findViewById(R.id.runtime);
        tvVoteAverage = findViewById(R.id.voteAverage);
        tvReleaseDate = findViewById(R.id.year);

        mCoordinatorLayout = findViewById(R.id.coordinator);

        btnFavorite = findViewById(R.id.btn_mark_favorite);
        setButtonFav();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String video_key = mMovieTrailer.getResults().get(clickedItemIndex).getKey();
        showTrailer(video_key);
    }

    private void showTrailer(String video_key){
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(ApiUtils.getBaseYoutubeVideos() + video_key));

        Intent chooser = Intent.createChooser(webIntent, "select an app");
        if(webIntent.resolveActivity(getPackageManager())!=null) {
            startActivity(chooser);
        }
    }

    //With SQLite
    public void onClickAddFavMovie(View view){
        addNewFavMov(id, posterPath, titleMovie);
        setButtonFav();
    }

    private void setButtonFav(){
        if (isValueInDB(id)) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);}
        else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);}
    }

    //with content resolver
    private void addNewFavMov(int id,String posterPath, String titleMovie){

        if(!isValueInDB(id)) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID, id);
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_TITLE, titleMovie);
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID, posterPath);

            //insert new movie data via a ContentResolver
            Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovie.CONTENT_URI, contentValues);
            if (uri != null) {
                showSnackBar(getString(R.string.added_to_fav));
            }
        }
        else if (isValueInDB(id)){
            if(deleteMovFromFav(id) > 0) {
                showSnackBar(getString(R.string.deleted_from_fav));
            }
        }
    }

    private void showSnackBar(String text){
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private int deleteMovFromFav(int id){
        int i;
        String stringId = Integer.toString(id);
        Uri uri = FavoriteMovieContract.FavoriteMovie.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        try{
            i = getContentResolver().delete(uri,null,null);
        }catch (Exception e){
            Log.d(TAG, "exception here ");
            e.printStackTrace();
            i = 0;
        }
        return  i;
    }

    private Cursor checkIfExist(int id){
        try {    return getContentResolver().query(FavoriteMovieContract.FavoriteMovie.CONTENT_URI,
                null,
                FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID+" = "+id,
                null,
                null,
                null);}

        catch (Exception e){
            Log.d(TAG, "failed to asynchronously load data");
            e.printStackTrace();
            return null;}
    }

    private boolean isValueInDB(int id){
        if(checkIfExist(id).getCount() < 1){ return false;}
        else return true;
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mDb!=null){mDb.close();}
    }

    @Override
    public void onStart(){
        super.onStart();
        FavoriteMovieDBHelper dbHelper = new FavoriteMovieDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }

    private void requestMovieTrailer(int movie_id, String api_key){
        ApiUtils.getApiService().getMovieTrailer(movie_id, api_key)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieTrailer>() {
                    @Override public void onCompleted() {}
                    @Override public void onError(Throwable e) {}
                    @Override public void onNext(MovieTrailer movieTrailer) {
                        mMovieTrailer = movieTrailer;
                        if(mRecyclerView!=null){mRecyclerView.removeAllViews();}
                        populateUIwithRecyclerView(movieTrailer);
                    }});
    }

    private void requestMovieReview(int movie_id, String api_key){
        ApiUtils.getApiService().getMovieReview(movie_id, api_key)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieReview>() {
                    @Override public void onCompleted() {}
                    @Override public void onError(Throwable e) {}
                    @Override public void onNext(MovieReview movieReview) {
                        //insert code here
                    }});
    }
}
