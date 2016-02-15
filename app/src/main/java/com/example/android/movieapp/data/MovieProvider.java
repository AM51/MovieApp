package com.example.android.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by archit.m on 25/12/15.
 */
public class MovieProvider extends ContentProvider{

    private MovieDbHelper movieDbHelper;
    private final static UriMatcher uriMatcher = buildUriMatcher();
    private final static int MOVIE = 100;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE,MOVIE);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case MOVIE : {
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,projection, selection,selectionArgs,null,null,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not a valid URI");
        }
        if( cursor != null)cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Not a valid URI");
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        switch (uriMatcher.match(uri)){
            case MOVIE: {
                long id = movieDbHelper.getWritableDatabase().insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                if(id > 0){
                    returnUri= MovieContract.MovieEntry.buildUriforMovie(id);
                } else {
                    throw new android.database.SQLException("Unable to insert into db for uri = "+uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Not a valid URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted;
        switch (uriMatcher.match(uri)){
            case MOVIE:
                rowsDeleted = movieDbHelper.getWritableDatabase().delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not a valid URI");
        }
        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
