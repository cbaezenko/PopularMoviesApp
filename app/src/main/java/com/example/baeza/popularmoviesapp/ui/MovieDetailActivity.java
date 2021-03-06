package com.example.baeza.popularmoviesapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.example.baeza.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by baeza on 13.03.2018.
 */

public class MovieDetailActivity extends AppCompatActivity {

    public final static String TITLE_KEY = "title_key";
    public final static String POSTER_PATH = "poster_path_key";
    public final static String OVERVIEW = "overview_key";
    public final static String RUNTIME = "runtime_key";
    public final static String VOTE_AVERAGE = "vote_average";
    public final static String RELEASE_DATE = "release_date";
    public final static String ID_MOVIE = "id";
    public final static String BACKDROP_PATH = "backdrop_path";

    @BindView(R.id.image)
    ImageView mImageView;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    private String titleMovie, posterPath, overview, runtime, voteAverage, release_date, backdropPath;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        ButterKnife.bind(this);

        getExtrasFromIntent();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(10);
        mTabLayout.setupWithViewPager(mViewPager);

        Picasso.with(this)
                .load(backdropPath)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(mImageView);

        mCollapsingToolbarLayout.setTitle(titleMovie);
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
        reviewFragment.setArguments(bundleReview());
        adapter.addFragment(reviewFragment, "Reviews");

        TrailerFragment trailerFragment = new TrailerFragment();
        trailerFragment.setArguments(bundleTrailer());
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
        bundle.putString(OverviewFragment.BACKDROP_PATH, backdropPath);
        return bundle;
    }

    private Bundle bundleReview() {
        Bundle bundle = new Bundle();
        bundle.putInt(ReviewFragment.MOVIE_ID, id);
        return bundle;
    }

    private Bundle bundleTrailer() {
        Bundle bundle = new Bundle();
        bundle.putInt(TrailerFragment.MOVIE_ID, id);
        return bundle;
    }
}
