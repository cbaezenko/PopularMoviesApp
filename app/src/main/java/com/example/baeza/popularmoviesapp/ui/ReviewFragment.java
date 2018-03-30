package com.example.baeza.popularmoviesapp.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baeza.popularmoviesapp.BuildConfig;
import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieReview.MovieReview;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterReview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baeza on 16.03.2018.
 */

public class ReviewFragment extends Fragment {

    public static final String MOVIE_ID = "movie_id";
    private MovieReview mMovieReview;
//    private RecyclerView mRecyclerView;

    private Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private static final String BUNDLE_REVIEW_STATE = "bundle_review";
    private static final String BUNDLE_CONTENT = "bundle_content";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int movie_id = getArguments().getInt(MOVIE_ID);

        if (savedInstanceState == null) {
            requestMovieReview(movie_id,
                    BuildConfig.KeyForMovies);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

//        mRecyclerView = new RecyclerView(container.getContext());
        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_REVIEW_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            mMovieReview = savedInstanceState.getParcelable(BUNDLE_CONTENT);
            fillRecycler(mMovieReview);
        }
        return view;
    }

    private void requestMovieReview(final int movie_id, String api_key) {
        ApiUtils.getApiService().getMovieReview(movie_id, api_key)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieReview>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(MovieReview movieReview) {
                        mMovieReview = movieReview;
                        fillRecycler(movieReview);
                    }
                });
    }

    public void fillRecycler(MovieReview movieReview) {
        RVAdapterReview rvAdapterReview = new RVAdapterReview(getContext(), movieReview);

        mRecyclerView.setAdapter(rvAdapterReview);

        //adding divider lines between recyclerview items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_REVIEW_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(BUNDLE_CONTENT, mMovieReview);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}