package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
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
public class FetchMovieReviews extends AsyncTask<Long,Void,ArrayList<MovieReview>> {

    private Context context;
    private String API_KEY;
    private  String LOG_TAG = getClass().getName();

    public FetchMovieReviews(Context context, String API_KEY) {
        this.context = context;
        this.API_KEY = API_KEY;
    }

    @Override
    protected ArrayList<MovieReview> doInBackground(Long... params) {
        Log.e("archit","do in backgroudn");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieInfo = null;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(params[0].toString())
                .appendPath("reviews")
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
            JSONObject movieJson = new JSONObject(movieInfo);
            JSONArray jsonArray = movieJson.getJSONArray("results");
            return getReviewList(jsonArray);
        } catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }
        return new ArrayList<>();
    }

    private ArrayList<MovieReview> getReviewList(JSONArray jsonArray) throws JSONException {

        ArrayList<MovieReview> movieReviews = new ArrayList<MovieReview>();
        for(int i=0;i< jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            MovieReview movieReview = new MovieReview();
            movieReview.setAuthor(jsonObject.getString("author"));
            movieReview.setContent(jsonObject.getString("content"));
            movieReviews.add(movieReview);
        }
        return movieReviews;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieReview> movieReviews) {
        Log.e("archit", "on post execute");
        super.onPostExecute(movieReviews);
        Intent intent = new Intent(context,MovieReviewActivity.class);
        intent.putParcelableArrayListExtra("reviews",movieReviews);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
