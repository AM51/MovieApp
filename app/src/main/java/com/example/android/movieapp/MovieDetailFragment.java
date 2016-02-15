package com.example.android.movieapp;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.movieapp.data.MovieContract;
import com.squareup.picasso.Picasso;


public class MovieDetailFragment extends Fragment implements FetchTrailerForShare.AsyncResponse{


    private static final String MOVIE_KEY = "saveMovie";
    RatingBar movieRating;
    ImageView moviePoster;
    TextView movieTitle;
    TextView movieOverview;
    TextView movieReleaseDate;
    TextView movieLanguage;
    CheckBox favMovies;
    Button playTrailer;
    Button showReviews;
    Long movieId;
    String posterPath;
    Double mRating;
    private ShareActionProvider mShareActionProvider;
    public String trailerURL="";
    private final String API_KEY = "eb228d863f83ab41f19e5f698f6a6f2a";
    private static final String movieSelection = MovieContract.MovieEntry.TABLE_NAME+"."+ MovieContract.MovieEntry.COLUMN_MOVIEID+"= ?";
    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("movieId", movieId);
        outState.putDouble("rating", movieRating.getRating());
        outState.putString("title", movieTitle.getText().toString());
        outState.putString("overview", movieOverview.getText().toString());
        outState.putString("release_date", movieReleaseDate.getText().toString());
        outState.putString("language", movieLanguage.getText().toString());
        outState.putString("poster_path", posterPath);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.moviedetail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        mShareActionProvider.setShareIntent(createShareForecastIntent());
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        FetchTrailerForShare forShare = new FetchTrailerForShare(getActivity(),API_KEY);
        forShare.delegate=this;
        try {
            forShare.execute(movieId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT,this.trailerURL  + " Play trailer here");
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movieRating = (RatingBar)rootView.findViewById(R.id.ratingBar);
        moviePoster = (ImageView)rootView.findViewById(R.id.movie_poster);
        movieTitle = (TextView)rootView.findViewById(R.id.movie_title);
        movieOverview = (TextView)rootView.findViewById(R.id.movie_overview);
        movieReleaseDate = (TextView)rootView.findViewById(R.id.movie_release_date);
        movieLanguage = (TextView)rootView.findViewById(R.id.movie_language);
        favMovies = (CheckBox) rootView.findViewById(R.id.addToFav);
        playTrailer = (Button) rootView.findViewById(R.id.playTrailer);
        showReviews = (Button) rootView.findViewById(R.id.showReviews);

        Intent intent= getActivity().getIntent();
        Bundle arguments = getArguments();

        if(savedInstanceState!=null){
            Log.e("mittal","from saved instance");

            movieId=savedInstanceState.getLong("movieId",0);
            Log.e("archit","rating from saved instance= "+savedInstanceState.getDouble("rating"));
            movieRating.setRating((float) (savedInstanceState.getDouble("rating", 1.0)));
            movieTitle.setText(savedInstanceState.getString("title")) ;
            movieOverview.setText(savedInstanceState.getString("overview"));
            movieReleaseDate.append(savedInstanceState.getString("release_date"));
            movieLanguage.append(savedInstanceState.getString("language"));
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + savedInstanceState.getString("poster_path")).fit().into(moviePoster);
            posterPath=savedInstanceState.getString("poster_path");
        } else if (arguments != null) {
            Log.e("mittal","from arguements");

            movieId = arguments.getLong("movieId",0);
            movieTitle.setText(arguments.getString("title"));
            movieOverview.setText(arguments.getString("overview"));
            movieReleaseDate.append(arguments.getString("release_date"));
            movieLanguage.append(arguments.getString("language"));
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + arguments.getString("poster_path")).fit().into(moviePoster);
            movieRating.setRating((float) (arguments.getDouble("rating", 1.0)));
            posterPath=arguments.getString("poster_path");
        }
        else if(intent.hasCategory("android.intent.category.LAUNCHER")){
            movieId =0L;
            movieTitle.setText("Please Select a movie");
        }
        else {
            Log.e("mittal","from intent "+intent.getDoubleExtra("rating",1.0));
            movieId = intent.getLongExtra("movieId", 0);
            movieTitle.setText(intent.getStringExtra("title"));
            movieOverview.setText(intent.getStringExtra("overview"));
            movieReleaseDate.append(intent.getStringExtra("release_date"));
            movieLanguage.append(intent.getStringExtra("language"));
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + intent.getStringExtra("poster_path")).fit().into(moviePoster);
            movieRating.setRating((float) (intent.getDoubleExtra("rating", 1.0)));
            posterPath=intent.getStringExtra("poster_path");
        }
        playTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchMovieTrailer(getActivity().getApplicationContext(), API_KEY).execute(movieId);
            }
        });

        showReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchMovieReviews(getActivity().getApplicationContext(),API_KEY).execute(movieId);
            }
        });

        Cursor isFav = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, movieSelection, new String[]{movieId.toString()}, null);
        if(isFav.moveToFirst()){
            favMovies.setChecked(true);
        }

        favMovies.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEID,movieId);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,movieTitle.getText().toString());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,movieOverview.getText().toString());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE,movieReleaseDate.getText().toString());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_LANGUAGE,movieLanguage.getText().toString());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING,movieRating.getRating());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,posterPath);
                    Log.e("archit","movie rating= "+movieRating.getRating()+" "+movieRating.getNumStars());
                    Cursor cursor=getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, movieSelection, new String[]{movieId.toString()}, null);
                    if(!cursor.moveToFirst()) {
                        Uri uri = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                        Long aLong = Long.valueOf(uri.getLastPathSegment());
                        cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
                        cursor.moveToFirst();
                        int ind = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEID);
                    }
                } else {
                    int rowsDeleted = getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,movieSelection,new String[]{movieId.toString()});
                    assert (rowsDeleted==1);
                }
            }
        });

        return rootView;
    }


    @Override
    public void processFinish(String output) {
        this.trailerURL = "http://www.youtube.com/watch?v="+output;
    }
}
