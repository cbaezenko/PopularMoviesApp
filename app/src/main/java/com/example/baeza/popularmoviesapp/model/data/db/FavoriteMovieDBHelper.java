package com.example.baeza.popularmoviesapp.model.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by baeza on 27.02.2018.
 */

public class FavoriteMovieDBHelper extends SQLiteOpenHelper {

    //this is the name where android will get the data base
    private static final String DATABASE_NAME = "favoriteMovie.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " +
                FavoriteMovieContract.FavoriteMovie.TABLE_NAME + " (" +
                FavoriteMovieContract.FavoriteMovie._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_IMAGE_URL_ID + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_TITLE + " TEXT NOT NULL" +
                ");";

        //execute the query to create the database
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.FavoriteMovie.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
