package com.example.baeza.popularmoviesapp.model.data.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieContract;
import com.example.baeza.popularmoviesapp.model.data.db.FavoriteMovieDBHelper;

/**
 * Created by baeza on 28.02.2018.
 */

public class MovieContentProvider extends ContentProvider{
    //It's convention to use 100, 200, 300, etc for directories,
    //and related ints (102, 102,..) for items in that directory.
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    private FavoriteMovieDBHelper mDBHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //add matches with addURI(String authority, String path, int code)
        //directory
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_MOVIES, MOVIES);
        //single item
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mDBHelper = new FavoriteMovieDBHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase database = mDBHelper.getReadableDatabase();
        //URI match code
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case MOVIES:{ //for all movies
                retCursor = database.query(FavoriteMovieContract.FavoriteMovie.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case MOVIES_WITH_ID:{
                //using selection and selectionArgs
                //URI: content://<authority>/movies/#
                //Index 0 would be the movie portion of the path
                //and index 1 is the segment right next to that

                String id = uri.getPathSegments().get(1);
                //Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};


                retCursor = database.query(FavoriteMovieContract.FavoriteMovie.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);


                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        //Set a notification URI on the Cursor, this way if anything changes in the URI,
        //the cursor will know
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //get access to the database and add new data
        final SQLiteDatabase database = mDBHelper.getWritableDatabase();
        //URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIES:{
                //insert values into Movies table
                long id = database.insertOrThrow(FavoriteMovieContract.FavoriteMovie.TABLE_NAME, null,contentValues);
                //if id is -1 means the insert was wrong
                    if ( id > 0 ){ //success
                        returnUri = ContentUris.withAppendedId(FavoriteMovieContract.BASE_CONTENT_URI, id);
                    }else {
                        throw new android.database.SQLException("Failed to insert row into "+ uri);
                    }
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri " + uri);
            }
        }
        //notify the resolver if the uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
