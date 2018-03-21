package com.example.baeza.popularmoviesapp.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieDBHelper;

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

    private SQLiteDatabase mDb;
    private static final String TAG = "OverviewFragment";

    ImageButton favoriteButton;
    TextView tv_overview, tv_runtime, tv_voteAverage, tv_year;
    String title, runtime, year, overview, voteAverage, poster_path;
    int id;


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

        tv_voteAverage.setText(voteAverage);
        tv_runtime.setText(runtime);
        tv_year.setText(year);
        tv_overview.setText(overview);

        setButtonFav();

        return view;
    }

    public void initLayoutComponents(View view) {
        favoriteButton = view.findViewById(R.id.btn_mark_favorite);
        tv_overview = view.findViewById(R.id.overview);
        tv_runtime = view.findViewById(R.id.runtime);
        tv_voteAverage = view.findViewById(R.id.voteAverage);
        tv_year = view.findViewById(R.id.year);
        favoriteButton.setOnClickListener(this);
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
        if (checkIfExist(id).getCount() < 1) {
            return false;
        } else return true;
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
            Log.d(TAG, "failed to asynchronously load data");
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
            Log.d(TAG, "exception here ");
            e.printStackTrace();
            i = 0;
        }
        return i;
    }

    private void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(getView(), text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
