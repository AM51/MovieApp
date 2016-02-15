package com.example.android.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements MainActivityFragment.Callback{

    boolean mTwoPane;
    private static final String MOVIEFRAGMENTTAG = "FTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container)!=null){
            mTwoPane = true;
            if(savedInstanceState==null) {
                MovieDetailFragment fragment = new MovieDetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, fragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        Log.e("archit",""+mTwoPane);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {  //maintain state of activity on back press
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MovieInfo movieInfo) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putLong("movieId",movieInfo.getMovieId());
            args.putString("title", movieInfo.getOriginal_title());
            args.putString("overview",movieInfo.getOverview());
            args.putString("language",movieInfo.getLanguage());
            args.putString("release_date",movieInfo.getRelease_date());
            args.putString("poster_path",movieInfo.getPoster_path());
            args.putDouble("rating",movieInfo.getRating());
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIEFRAGMENTTAG)
                    .commit();
            Log.e("archit","setting args");
        } else {
            Intent intent = new Intent(this,MovieDetailActivity.class);
            intent.putExtra("title", movieInfo.getOriginal_title());
            intent.putExtra("overview", movieInfo.getOverview());
            intent.putExtra("poster_path", movieInfo.getPoster_path());
            intent.putExtra("release_date", movieInfo.getRelease_date());
            intent.putExtra("language", movieInfo.getLanguage());

            intent.putExtra("rating", movieInfo.getRating());
            intent.putExtra("movieId", movieInfo.getMovieId());
            startActivity(intent);
        }
    }
}
