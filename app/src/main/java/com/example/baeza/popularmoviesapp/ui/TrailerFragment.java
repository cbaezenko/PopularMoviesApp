package com.example.baeza.popularmoviesapp.ui;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.BuildConfig;
import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieTrailer.MovieTrailer;
import com.example.baeza.popularmoviesapp.model.data.network.utilities.ApiUtils;
import com.example.baeza.popularmoviesapp.ui.adapters.RVAdapterTrailer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baeza on 16.03.2018.
 */

public class TrailerFragment extends Fragment implements RVAdapterTrailer.ListItemClickListener {

    public static final String MOVIE_ID = "movie_id";
    private MovieTrailer mMovieTrailer;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private static final String BUNDLE_TRAILER_STATE = "bundle_trailer_position";
    private static final String BUNDLE_TRAILER_CONTENT = "bundle_trailer_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int movie_id = getArguments().getInt(MOVIE_ID);

        if (savedInstanceState == null) {
            requestMovieTrailer(movie_id,
                    BuildConfig.KeyForMovies);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_TRAILER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            mMovieTrailer = savedInstanceState.getParcelable(BUNDLE_TRAILER_CONTENT);
            fillRecycler(mMovieTrailer);
        }

        return view;
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
                        fillRecycler(movieTrailer);
                    }
                });
    }

    private void fillRecycler(MovieTrailer movieTrailer) {
        RVAdapterTrailer rvAdapterTrailer = new RVAdapterTrailer(getContext(), movieTrailer, this);

        mRecyclerView.setAdapter(rvAdapterTrailer);

        //adding divider lines between recyclerview items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_TRAILER_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(BUNDLE_TRAILER_CONTENT, mMovieTrailer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
