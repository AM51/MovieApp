package com.example.android.movieapp;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.android.movieapp.data.MovieContract;
import com.example.android.movieapp.data.MovieDbHelper;
import com.example.android.movieapp.data.MovieProvider;

import java.util.Map;
import java.util.Set;

/**
 * Created by archit.m on 25/12/15.
 */
public class MovieTest extends AndroidTestCase {

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null
        );


        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
        cursor.close();

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromProvider();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }
    public void testMovieInsertQuery(){

        MovieDbHelper movieDbHelper = new MovieDbHelper(mContext);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEID, 1481);
        long id = movieDbHelper.getWritableDatabase().insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
        assertTrue(id != -1);
        movieDbHelper.close();

        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);

        assertTrue(cursor.moveToFirst());
        assertTrue(cursor != null);
        //cursor.moveToPosition(11);
        Set<Map.Entry<String, Object>> entries = contentValues.valueSet();
        for(Map.Entry<String,Object> entry : entries){
            String columnName = entry.getKey();
            int ind = cursor.getColumnIndex(columnName);
            assertEquals(columnName, MovieContract.MovieEntry.COLUMN_MOVIEID);
            assertEquals(ind,0);
            int movieId = cursor.getInt(ind);
            int expval = (int) entry.getValue();
            assertEquals(movieId,1481);
        }
    }
}
