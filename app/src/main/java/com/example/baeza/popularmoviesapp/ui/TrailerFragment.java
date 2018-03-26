package com.example.baeza.popularmoviesapp.ui;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.BuildConfig;
import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieTrailer.MovieTrailer;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterTrailer;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baeza on 16.03.2018.
 */

public class TrailerFragment extends Fragment implements RVAdapterTrailer.ListItemClickListener {

    private RecyclerView mRecyclerView;
    public static final String MOVIE_ID = "movie_id";
    private int movie_id;
    private MovieTrailer mMovieTrailer;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie_id = getArguments().getInt(MOVIE_ID);
        requestMovieTrailer(movie_id,
                BuildConfig.KeyForMovies);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        mRecyclerView = new RecyclerView(getContext());

        fillRecycler();

        return mRecyclerView;
    }


    private void requestMovieTrailer(int movie_id, String api_key) {
        ApiUtils.getApiService().getMovieTrailer(movie_id, api_key)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieTrailer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(MovieTrailer movieTrailer) {
                        mMovieTrailer = movieTrailer;
                        fillRecycler();
                    }
                });
    }

    private void fillRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RVAdapterTrailer rvAdapterTrailer = new RVAdapterTrailer(getContext(), mMovieTrailer, this);

        mRecyclerView.setAdapter(rvAdapterTrailer);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        //adding divider lines between recyclerview items
        mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String video_key = mMovieTrailer.getResults().get(clickedItemIndex).getKey();
        showTrailer(video_key);
        if (clickedItemIndex == R.id.shareButton) {
            Toast.makeText(getContext(), "selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void showTrailer(String video_key) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(ApiUtils.getBaseYoutubeVideos() + video_key));

        Intent chooser = Intent.createChooser(webIntent, getString(R.string.select_app));
        if (webIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }
    }


}
