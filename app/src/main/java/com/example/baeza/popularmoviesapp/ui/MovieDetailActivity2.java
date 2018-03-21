package com.example.baeza.popularmoviesapp.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.example.baeza.popularmoviesapp.model.data.network.model.movieReview.MovieReview;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieTrailer.MovieTrailer;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    ImageView imageView;
    private MovieTrailer mMovieTrailer;
    private MovieReview mMovieReview;
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


        imageView = findViewById(R.id.image);
        Picasso.with(this).load(backdropPath).into(imageView);

        Log.d(TAG, "poster path " + posterPath);
        Log.d(TAG, "backdrop is " + backdropPath);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(titleMovie);

//        getInfo(id, getString(R.string.key_movies));

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

        Log.d(TAG, titleMovie + "\n" +
                overview + "\n" +
                runtime + "\n" +
                release_date);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        OverviewFragment overviewFragment = new OverviewFragment();
        overviewFragment.setArguments(bundleOverview());

        adapter.addFragment(overviewFragment, "Overview");

        ReviewFragment reviewFragment = new ReviewFragment();
        reviewFragment.setArguments(bundleReview());

        adapter.addFragment(reviewFragment, "Reviews");

        adapter.addFragment(new TrailerFragment(), "Trailers");
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
        return bundle;
    }

    private Bundle bundleReview() {
        Bundle bundle = new Bundle();
        bundle.putInt(ReviewFragment.MOVIE_ID, id);
        return bundle;
    }

//    private void getInfo(int movie_id, String api_key) {
//        requestMovieReview(movie_id, api_key);
//        requestMovieTrailer(movie_id, api_key);
//    }


//    private void requestMovieTrailer(int movie_id, String api_key) {
//        ApiUtils.getApiService().getMovieTrailer(movie_id, api_key)
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MovieTrailer>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(MovieTrailer movieTrailer) {
//                        Log.d(TAG, "movie trailers" + movieTrailer.getResults().toString());
//                    }
//                });
//    }

//    private void requestMovieReview(final int movie_id, String api_key) {
//        ApiUtils.getApiService().getMovieReview(movie_id, api_key)
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MovieReview>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(MovieReview movieReview) {
//                        Log.d(TAG, "movie review is : " + movieReview.getResults().get(0).getContent());
//                        //insert code here
//                        mMovieReview = movieReview;
//                        bundleReview(movieReview);
//
//                    }
//                });
//    }
}
