package com.example.baeza.popularmoviesapp;

import android.os.AsyncTask;
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
import com.example.baeza.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

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

    private RecyclerView mRecyclerView;
    private RecyclerAdapterDetailMovie mRecyclerAdapterDetailMovie;
    private ImageView iv_poster;
    private TextView tvTitle, tvOverview, tvRunTime, tvVoteAverage, tvReleaseDate;
    private String titleMovie, posterPath, overview, runtime, voteAverage, release_date;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);

        getExtrasFromIntent();
        initUIItems();
        populateUIwithRecyclerView();

        tvTitle.setText(titleMovie);
        tvOverview.setText(overview);
        tvRunTime.setText(runtime+getString(R.string.minutes));
        tvVoteAverage.setText(voteAverage+getString(R.string.vote_average_design));
        tvReleaseDate.setText(getOnlyYear(release_date));
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
    }

    private void initUIItems(){
        tvTitle = findViewById(R.id.tv_title);
        iv_poster = findViewById(R.id.iv_poster);
        tvOverview = findViewById(R.id.overview);
        tvRunTime = findViewById(R.id.runtime);
        tvVoteAverage = findViewById(R.id.voteAverage);
        tvReleaseDate = findViewById(R.id.year);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return true;
    }

    public class AskForTrailers extends AsyncTask<URL, Void, String>{
        @Override
        protected String doInBackground(URL... urls) {
            URL url =urls[0];
            String jsonTrailersResponse = null;
            try{
                jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(url);
                JsonUtilities.parseMovieTrailerJSON(jsonTrailersResponse);
                return  jsonTrailersResponse;
            }catch (Exception e){
                e.printStackTrace();}
            return null;
        }
        @Override
        protected void onPostExecute(String moviesData){
            if(moviesData!=null && !moviesData.equals("")){
            //add code here
            }
        }
    }


//    private void setForCollapsing(){
        //        tv_title = findViewById(R.id.tv_title);
//        mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        textToShow = findViewById(R.id.textToShow);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        CollapsingToolbarLayout collapsingToolbarLayout =findViewById(R.id.collapsing_toolbar);
//        //collapsingToolbarLayout.setTitle("MovieDetailActivity");
//        tv_title.setText(titleMovie);
//    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(MovieDetailActivity.this, "item "+clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}