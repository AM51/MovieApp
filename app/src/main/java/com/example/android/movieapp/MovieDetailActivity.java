package com.example.android.movieapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MovieDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if(savedInstanceState == null){
            MovieDetailFragment fragment = new MovieDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}
