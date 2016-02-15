package com.example.android.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapp.data.MovieContract;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by archit.m on 02/01/16.
 */
public class FetchFavMovies extends AsyncTask<Void,Void,ArrayList<MovieInfo>> {

    private Context context;
    ImageAdapter imageAdapter;
    private final String API_KEY;
    private String LOG_TAG = getClass().getName();

    public FetchFavMovies(Context context, ImageAdapter imageAdapter, String api_key) {
        this.context = context;
        this.imageAdapter = imageAdapter;
        API_KEY = api_key;
    }

    private MovieInfo getMovieInfo(Long movieId){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieInfo = null;

        Uri.Builder builder=new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId.toString())
                .appendQueryParameter("api_key", API_KEY);


        URL url = null;
        try {
            url = new URL(builder.build().toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieInfo = buffer.toString();
            MovieInfo movie = new MovieInfo();
            JSONObject movieJson = new JSONObject(movieInfo);
            movie.setMovieId(movieJson.getLong("id"));
            if(MStringUtils.isValid(movieJson.getString("original_title"))){
                movie.setOriginal_title(movieJson.getString("original_title"));
            } else {
                movie.setOriginal_title("Not Available At The Moment");
            }

            movie.setPoster_path(movieJson.getString("poster_path"));

            if(MStringUtils.isValid(movieJson.getString("release_date"))){
                movie.setRelease_date(movieJson.getString("release_date"));
            } else {
                movie.setRelease_date("Not Available At The Moment");
            }

            if(MStringUtils.isValid(movieJson.getString("overview"))){
                movie.setOverview(movieJson.getString("overview"));
            } else {
                movie.setOverview("Not Available At The Moment");
            }

            if(MStringUtils.isValid(movieJson.getString("original_language"))){
                movie.setLanguage(movieJson.getString("original_language"));
            } else {
                movie.setLanguage("Not Available At The Moment");
            }

            movie.setRating(movieJson.getDouble("vote_average"));
            movie.setPopularity(movieJson.getDouble("popularity"));
            movie.setMovieId(movieJson.getLong("id"));
            if (!movie.getPoster_path().equals("null")){
                Log.v("archit",""+movie);
                return movie;
            }
        } catch (Exception e){
            Log.e(LOG_TAG,e.getMessage());
        }

        return null;
    }
    @Override
    protected ArrayList<MovieInfo> doInBackground(Void... params) {
        Log.e("archit","fetching fav movies from db");
        Cursor cursor=context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        int ind = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEID);
        MovieInfo movieInfo;
        ArrayList movieList = new ArrayList();

        while (cursor.moveToNext()){
            Long movieId = cursor.getLong(ind);
            Log.v("archit","fav movie id = "+movieId);
            movieInfo = getMovieInfo(movieId);
            if(movieInfo!=null){
                movieList.add(movieInfo);
            }
        }
        return movieList;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieInfo> movieList) {
        super.onPostExecute(movieList);
        if (movieList == null) {
            return;
        }

//        if(movieList.size()%2==1){
//            movieList.remove(movieList.size()-1);
//        }
        imageAdapter.clear();
        imageAdapter.setmThumbIds(movieList);
        imageAdapter.notifyDataSetChanged();
    }
}
