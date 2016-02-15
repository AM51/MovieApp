package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by archit.m on 02/01/16.
 */
public class FetchMovieTrailer extends AsyncTask<Long,Void,String> {

    private Context context;
    private String API_KEY;
    private  String LOG_TAG = getClass().getName();

    public FetchMovieTrailer(Context context, String API_KEY) {
        this.context = context;
        this.API_KEY = API_KEY;
    }

    @Override
    protected String doInBackground(Long... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieInfo = null;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(params[0].toString())
                .appendPath("videos")
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
            if(jsonArray.length()>0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                return jsonObject.getString("key");
            }
        } catch (Exception e){
            Log.e(LOG_TAG,e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s==null){
            Toast.makeText(context, "No trailer available", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+s));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
