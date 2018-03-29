package com.example.baeza.popularmoviesapp.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieDBHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by baeza on 16.03.2018.
 */

public class OverviewFragment extends android.support.v4.app.Fragment implements OnClickListener {

    public final static String TITLE_KEY = "title_key";
    public final static String OVERVIEW = "overview_key";
    public final static String RUNTIME = "runtime_key";
    public final static String VOTE_AVERAGE = "vote_average";
    public final static String RELEASE_DATE = "release_date";
    public final static String MOVIE_ID = "id";
    public final static String POSTER_PATH = "poster_path";
    public final static String BACKDROP_PATH = "backdrop_path";

    private SQLiteDatabase mDb;

    private ImageView ivPoster;
    private ImageButton favoriteButton;
    private TextView tv_overview, tv_runtime, tv_voteAverage, tv_year, tv_minutes;
    private String title, runtime, year, overview, voteAverage, poster_path, backdrop_path;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.overview_fragment, container, false);
        initLayoutComponents(view);

        title = getArguments().getString(TITLE_KEY);
        runtime = getArguments().getString(RUNTIME);
        year = getArguments().getString(RELEASE_DATE);
        overview = getArguments().getString(OVERVIEW);
        voteAverage = getArguments().getString(VOTE_AVERAGE);
        poster_path = getArguments().getString(POSTER_PATH);
        id = getArguments().getInt(MOVIE_ID);
        backdrop_path = getArguments().getString(BACKDROP_PATH);

        tv_voteAverage.setText(voteAverage);
        tv_runtime.setText(runtime);
        tv_year.setText(getOnlyYear(year));
        tv_overview.setText(overview);

        tv_minutes.setText(getString(R.string.minutes));

        setButtonFav();
        setImagePoster(poster_path);

        return view;
    }

    public void initLayoutComponents(View view) {
        favoriteButton = view.findViewById(R.id.btn_mark_favorite);
        tv_overview = view.findViewById(R.id.overview);
        tv_runtime = view.findViewById(R.id.runtime);
        tv_voteAverage = view.findViewById(R.id.voteAverage);
        tv_year = view.findViewById(R.id.year);

        tv_minutes = view.findViewById(R.id.tv_minutes);

        ivPoster = view.findViewById(R.id.iv_poster);
        favoriteButton.setOnClickListener(this);
    }

    private void setImagePoster(String poster_path) {
        Picasso.with(getContext())
                .load(poster_path)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(ivPoster);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mark_favorite: {
                addNewFavMov(id, poster_path, title);
                setButtonFav();
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FavoriteMovieDBHelper dbHelper = new FavoriteMovieDBHelper(getContext());
        mDb = dbHelper.getWritableDatabase();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDb != null) {
            mDb.close();
        }
    }

    private void setButtonFav() {
        if (isValueInDB(id)) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private boolean isValueInDB(int id) {
        return checkIfExist(id).getCount() >= 1;
    }

    private Cursor checkIfExist(int id) {
        try {
            return getActivity().getContentResolver().query(FavoriteMovieContract.FavoriteMovie.CONTENT_URI,
                    null,
                    FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " = " + id,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //with content resolver
    private void addNewFavMov(int id, String posterPath, String titleMovie) {

        if (!isValueInDB(id)) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID, id);
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_TITLE, titleMovie);
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID, posterPath);
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_USER_RATING, Double.valueOf(voteAverage));
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_RELEASE_DATE, year);
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_BACKDROP_IMAGE, backdrop_path );
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_OVERVIEW, overview);
            contentValues.put(FavoriteMovieContract.FavoriteMovie.COLUMN_RUNTIME, Integer.valueOf(runtime));
            //insert new movie data via a ContentResolver
            Uri uri = getActivity().getContentResolver().insert(FavoriteMovieContract.FavoriteMovie.CONTENT_URI, contentValues);
            if (uri != null) {
                showSnackBar(getString(R.string.added_to_fav));
            }
        } else if (isValueInDB(id)) {
            if (deleteMovFromFav(id) > 0) {
                showSnackBar(getString(R.string.deleted_from_fav));
            }
        }
    }

    private int deleteMovFromFav(int id) {
        int i;
        String stringId = Integer.toString(id);
        Uri uri = FavoriteMovieContract.FavoriteMovie.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        try {
            i = getActivity().getContentResolver().delete(uri, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            i = 0;
        }
        return i;
    }

    private void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(getView(), text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @NonNull
    private String getOnlyYear(String release_date) {
        return release_date.substring(0, 4);
    }
}
