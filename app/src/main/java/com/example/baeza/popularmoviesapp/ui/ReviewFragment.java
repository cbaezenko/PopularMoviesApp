package com.example.baeza.popularmoviesapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieReview.MovieReview;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterReview;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baeza on 16.03.2018.
 */

public class ReviewFragment extends Fragment {

    public static final String MOVIE_ID = "movie_id";
    private int movie_id;

    private MovieReview mMovieReview;
    private RecyclerView mRecyclerView;
    private DividerItemDecoration mDividerItemDecoration;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie_id = getArguments().getInt(MOVIE_ID);
        requestMovieReview(movie_id, getString(R.string.key_movies));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        mRecyclerView = new RecyclerView(container.getContext());
        fillRecycler();

        return mRecyclerView;
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
                        fillRecycler();
                    }
                });
    }

    public void fillRecycler(){
        RVAdapterReview rvAdapterReview = new RVAdapterReview(getContext(), mMovieReview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(rvAdapterReview);

        //adding divider lines between recyclerview items
        mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
    }
}