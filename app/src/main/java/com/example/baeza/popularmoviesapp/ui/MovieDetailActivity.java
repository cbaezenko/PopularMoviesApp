package com.example.baeza.popularmoviesapp.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieDBHelper;
import com.example.baeza.popularmoviesapp.ui.adapters.RecyclerAdapterDetailMovie;
import com.squareup.picasso.Picasso;

/**
 * Created by baeza on 16.02.2018.
 */

public class MovieDetailActivity extends AppCompatActivity implements RecyclerAdapterDetailMovie.ListItemClickListener {

    public final static String TITLE_KEY = "title_key";
    public final static String POSTER_PATH = "poster_path_key";
    public final static String OVERVIEW = "overview_key";
    public final static String RUNTIME = "runtime_key";
    public final static String VOTE_AVERAGE = "vote_average";
    public final static String RELEASE_DATE = "release_date";
    public final static String ID_MOVIE = "id";

    protected static final String TAG = "MovieDetailActivity";

    private SQLiteDatabase mDb;

    private RecyclerView mRecyclerView;
    private RecyclerAdapterDetailMovie mRecyclerAdapterDetailMovie;
    private ImageView iv_poster;
    private TextView tvTitle, tvOverview, tvRunTime, tvVoteAverage, tvReleaseDate;
    private String titleMovie, posterPath, overview, runtime, voteAverage, release_date;
    private int id;
    Button btnFavorite;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);

        //with bd SQLite
        FavoriteMovieDBHelper dbHelper = new FavoriteMovieDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        getExtrasFromIntent();
        initUIItems();
        populateUIwithRecyclerView();

        tvTitle.setText(titleMovie);
        tvOverview.setText(overview);
        tvRunTime.setText(runtime+getString(R.string.minutes));
        tvVoteAverage.setText(voteAverage+getString(R.string.vote_average_design));
        tvReleaseDate.setText(getOnlyYear(release_date));
        Log.d(TAG,"poster path "+posterPath);
        Picasso.with(this).load(posterPath).into(iv_poster);
    }

    private String getOnlyYear(String release_date){
        String yearRelease = release_date.substring(0,4);
        return yearRelease;
    }

    private void populateUIwithRecyclerView(){
        mRecyclerView = findViewById(R.id.rv_trailers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerAdapterDetailMovie = new RecyclerAdapterDetailMovie(MovieDetailActivity.this, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapterDetailMovie);
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

        btnFavorite = findViewById(R.id.btn_mark_favorite);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(MovieDetailActivity.this, "item "+clickedItemIndex, Toast.LENGTH_SHORT).show();
    }


    //With SQLite
    public void addToFavoriteMovie(View view){
        addNewFavoriteMovie(id, posterPath);
//
//        Cursor cursor = getAllMovies();
//        cursor.moveToFirst();
//        String showPoster = cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID));
//        int showId = cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID));
//        Log.d(TAG, "count from database "+cursor.getCount()+" from Database: poster: "+showPoster+"\n"
//        +" show id: "+showId);
//        cursor.close();
    }

    private void addNewFavoriteMovie(int id, String poster_path){
        try{
            ContentValues cv = new ContentValues();
            cv.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID, id);
            cv.put(FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID,poster_path);
            mDb.insertOrThrow(FavoriteMovieContract.FavoriteMovie.TABLE_NAME, null, cv);
            Toast.makeText(this, "added to favorite", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "already in favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private Cursor getAllMovies(){
        return mDb.query(
                FavoriteMovieContract.FavoriteMovie.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
