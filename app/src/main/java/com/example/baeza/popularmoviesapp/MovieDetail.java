package com.example.baeza.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.model.RecyclerAdapterDetailMovie;
import com.example.baeza.popularmoviesapp.utilities.JsonUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

/**
 * Created by baeza on 16.02.2018.
 */

public class MovieDetail extends AppCompatActivity implements RecyclerAdapterDetailMovie.ListItemClickListener {

    public final static String TITLE_KEY = "title_key";
    public final static String POSTER_PATH = "poster_path_key";
    public final static String OVERVIEW = "overview_key";
    public final static String RUNTIME = "runtime_key";
    public final static String VOTE_AVERAGE = "vote_average";

    private RecyclerView mRecyclerView;
    private RecyclerAdapterDetailMovie mRecyclerAdapterDetailMovie;
    private ImageView iv_poster;
    private TextView tvTitle, tvOverview, tvRunTime, tvVoteAverage;
    private String titleMovie, posterPath, overview, runtime, voteAverage;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);

        titleMovie = getIntent().getStringExtra(TITLE_KEY);
        posterPath = getIntent().getStringExtra(POSTER_PATH);
        overview = getIntent().getStringExtra(OVERVIEW);
        voteAverage = Double.toString(getIntent().getDoubleExtra(VOTE_AVERAGE, 0.0));
        runtime = Integer.toString(getIntent().getIntExtra(RUNTIME, 0));

        initUIItems();
        populateUIwithRecyclerView();

        tvTitle.setText(titleMovie);
        tvOverview.setText(overview);
        tvRunTime.setText(runtime+"min");
        tvVoteAverage.setText(voteAverage+"/10");
        Picasso.with(this).load(posterPath).into(iv_poster);
    }

    private void populateUIwithRecyclerView(){
        mRecyclerView = findViewById(R.id.rv_trailers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MovieDetail.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerAdapterDetailMovie = new RecyclerAdapterDetailMovie(MovieDetail.this, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapterDetailMovie);
    }

    private void initUIItems(){
        tvTitle = findViewById(R.id.tv_title);
        iv_poster = findViewById(R.id.iv_poster);
        tvOverview = findViewById(R.id.overview);
        tvRunTime = findViewById(R.id.runtime);
        tvVoteAverage = findViewById(R.id.voteAverage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void setForCollapsing(){
        //        tv_title = findViewById(R.id.tv_title);
//        mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        textToShow = findViewById(R.id.textToShow);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        CollapsingToolbarLayout collapsingToolbarLayout =findViewById(R.id.collapsing_toolbar);
//        //collapsingToolbarLayout.setTitle("MovieDetail");
//        tv_title.setText(titleMovie);
    }

    private void askForTrailers() throws JSONException {
        JsonUtilities.parseMovieTrailerJSON("string json");
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(MovieDetail.this, "item "+clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
