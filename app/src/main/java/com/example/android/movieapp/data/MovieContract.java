package com.example.android.movieapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by archit.m on 25/12/15.
 */
public class MovieContract {

    public  final static String CONTENT_AUTHORITY = "com.example.android.movieapp";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public final static String PATH_MOVIE =  "movie";

    public final static class MovieEntry  {

        public final static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public final static String TABLE_NAME = "movies";

        public final static String COLUMN_MOVIEID = "movie_id";

        public final static String COLUMN_MOVIE_TITLE = "movie_title";
        
        public final static String COLUMN_MOVIE_OVERVIEW = "movie_overview";

        public final static String COLUMN_MOVIE_DATE = "release_date";

        public final static String COLUMN_MOVIE_LANGUAGE = "language";

        public final static String COLUMN_MOVIE_RATING = "rating";

        public final static String COLUMN_POSTER_PATH = "poster_path";

        public static Uri buildUriforMovie(Long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
