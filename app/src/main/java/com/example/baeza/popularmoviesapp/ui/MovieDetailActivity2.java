package com.example.baeza.popularmoviesapp.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieDBHelper;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieReview.MovieReview;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieTrailer.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baeza on 13.03.2018.
 */

public class MovieDetailActivity2 extends AppCompatActivity {

    public final static String TITLE_KEY = "title_key";
    public final static String POSTER_PATH = "poster_path_key";
    public final static String OVERVIEW = "overview_key";
    public final static String RUNTIME = "runtime_key";
    public final static String VOTE_AVERAGE = "vote_average";
    public final static String RELEASE_DATE = "release_date";
    public final static String ID_MOVIE = "id";
    public final static String BACKDROP_PATH = "backdrop_path";

    private String titleMovie, posterPath, overview, runtime, voteAverage, release_date, backdropPath;
    private int id;

    private SQLiteDatabase mDb;
    private CoordinatorLayout mCoordinatorLayout;



    ImageView imageView;
    ImageButton btnFavorite;

    CollapsingToolbarLayout collapsingToolbarLayout;

    private final static String TAG = "MovieDetailActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail2);

        getExtrasFromIntent();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        btnFavorite = findViewById(R.id.btn_mark_favorite);
        mCoordinatorLayout = findViewById(R.id.coordinator);
        imageView = findViewById(R.id.image);
//        setButtonFav();

        Picasso.with(this).load(backdropPath).into(imageView);

        Log.d(TAG, "poster path " + posterPath);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(titleMovie);

    }

    public void getExtrasFromIntent() {
        titleMovie = getIntent().getStringExtra(TITLE_KEY);
        posterPath = getIntent().getStringExtra(POSTER_PATH);
        overview = getIntent().getStringExtra(OVERVIEW);
        voteAverage = Double.toString(getIntent().getDoubleExtra(VOTE_AVERAGE, 0.0));
        runtime = Integer.toString(getIntent().getIntExtra(RUNTIME, 0));
        release_date = getIntent().getStringExtra(RELEASE_DATE);
        id = getIntent().getIntExtra(ID_MOVIE, 1);
        backdropPath = getIntent().getStringExtra(BACKDROP_PATH);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        OverviewFragment overviewFragment = new OverviewFragment();
        overviewFragment.setArguments(bundleOverview());
        adapter.addFragment(overviewFragment, "Overview");

        ReviewFragment reviewFragment = new ReviewFragment();
        reviewFragment.setArguments(bundleReviewTrailer());
        adapter.addFragment(reviewFragment, "Reviews");

        TrailerFragment trailerFragment = new TrailerFragment();
        trailerFragment.setArguments(bundleReviewTrailer());
        adapter.addFragment(trailerFragment, "Trailers");

        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private Bundle bundleOverview() {
        Bundle bundle = new Bundle();
        bundle.putString(OverviewFragment.TITLE_KEY, titleMovie);
        bundle.putString(OverviewFragment.OVERVIEW, overview);
        bundle.putString(OverviewFragment.RELEASE_DATE, release_date);
        bundle.putString(OverviewFragment.RUNTIME, runtime);
        bundle.putString(OverviewFragment.VOTE_AVERAGE, voteAverage);
        bundle.putString(OverviewFragment.POSTER_PATH, posterPath);
        bundle.putInt(OverviewFragment.MOVIE_ID, id);
        return bundle;
    }

    private Bundle bundleReviewTrailer() {
        Bundle bundle = new Bundle();
        bundle.putInt(ReviewFragment.MOVIE_ID, id);
        return bundle;
    }


/////////////////////////////
//    public void onClickAddFavMovie(View view) {
//        addNewFavMov(id, posterPath, titleMovie);
//        setButtonFav();
//    }
/////////////////////////////

//
//    @Override
//    public void onStart() {
//        super.onStart();
//        FavoriteMovieDBHelper dbHelper = new FavoriteMovieDBHelper(this);
//        mDb = dbHelper.getWritableDatabase();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mDb != null) {
//            mDb.close();
//        }
//    }
//
//    private void setButtonFav() {
//        if (isValueInDB(id)) {
//            btnFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
//        } else {
//            btnFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//        }
//    }
//
//    private boolean isValueInDB(int id) {
//        if (checkIfExist(id).getCount() < 1) {
//            return false;
//        } else return true;
//    }
//
//    private Cursor checkIfExist(int id) {
//        try {
//            return getContentResolver().query(FavoriteMovieContract.FavoriteMovie.CONTENT_URI,
//                    null,
//                    FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " = " + id,
//                    null,
//                    null,
//                    null);
//        } catch (Exception e) {
//            Log.d(TAG, "failed to asynchronously load data");
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    //with content resolver
//    private void addNewFavMov(int id, String posterPath, String titleMovie) {
//
//        if (!isValueInDB(id)) {
//
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID, id);
//            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_TITLE, titleMovie);
//            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID, posterPath);
//
//            //insert new movie data via a ContentResolver
//            Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovie.CONTENT_URI, contentValues);
//            if (uri != null) {
//                showSnackBar(getString(R.string.added_to_fav));
//            }
//        } else if (isValueInDB(id)) {
//            if (deleteMovFromFav(id) > 0) {
//                showSnackBar(getString(R.string.deleted_from_fav));
//            }
//        }
//    }
//
//    private int deleteMovFromFav(int id) {
//        int i;
//        String stringId = Integer.toString(id);
//        Uri uri = FavoriteMovieContract.FavoriteMovie.CONTENT_URI;
//        uri = uri.buildUpon().appendPath(stringId).build();
//        try {
//            i = getContentResolver().delete(uri, null, null);
//        } catch (Exception e) {
//            Log.d(TAG, "exception here ");
//            e.printStackTrace();
//            i = 0;
//        }
//        return i;
//    }
//
//    private void showSnackBar(String text) {
//        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_SHORT);
//        snackbar.show();
//    }
}
