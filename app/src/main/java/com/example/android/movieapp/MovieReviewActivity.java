package com.example.android.movieapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class MovieReviewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("archit", "create review activity");
        setContentView(R.layout.activity_movie_review);
        ListView movieReviews = (ListView)findViewById(R.id.reviews);
        ArrayList reviews = getIntent().getParcelableArrayListExtra("reviews");
        Log.e("archit",""+reviews.size());
        movieReviews.setAdapter(new ReviewAdapter(getApplicationContext(),reviews));
    }

}
