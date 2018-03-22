package com.example.baeza.popularmoviesapp.model.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by baeza on 27.02.2018.
 */

public class FavoriteMovieContract {

    //The authority, which is how your code knows which Content provider to access.
    public static final String AUTHORITY = "com.example.baeza.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //this is the path for the "movies" directory
    public static final String PATH_MOVIES = "movies";

    private FavoriteMovieContract() {
    }

    public static class FavoriteMovie implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie_favorite";
        public static final String COLUMN_IMAGE_URL_ID = "image";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_BACKDROP_IMAGE = "backdrop_image";
        public static final String COLUMN_MOVIE_OVERVIEW = "synopsis";
        public static final String COLUMN_RELEASE_DATE ="release_date";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RUNTIME = "runtime";


    }

}
