package com.example.android.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by archit.m on 25/12/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;

    private static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIE_TABLE = "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME + " (  "+
//                MovieContract.MovieEntry._ID +" INTEGER PRIMARY KEY ," +
                MovieContract.MovieEntry.COLUMN_MOVIEID + " LONG PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE +" VARCHAR(100), "+
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW+" VARCHAR(100), "+
                MovieContract.MovieEntry.COLUMN_MOVIE_DATE+" VARCHAR(100), "+
                MovieContract.MovieEntry.COLUMN_MOVIE_LANGUAGE+" VARCHAR(100), "+
                MovieContract.MovieEntry.COLUMN_MOVIE_RATING+" DOUBLE, "+
                MovieContract.MovieEntry.COLUMN_POSTER_PATH+" VARCHAR(100) "+
                " );";

        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
