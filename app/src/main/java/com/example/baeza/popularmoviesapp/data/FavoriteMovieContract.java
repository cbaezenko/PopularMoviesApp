package com.example.baeza.popularmoviesapp.data;

import android.provider.BaseColumns;

/**
 * Created by baeza on 27.02.2018.
 */

public class FavoriteMovieContract {

    private FavoriteMovieContract(){}

    public static class MovieFavorite implements BaseColumns{
        public static final String TABLE_NAME = "movie_favorite";
        public static final String COLUMN_IMAGE_URL_ID = "image";
        public static final String COLUMN_MOVIE_ID = "movie_id";

    }
}
