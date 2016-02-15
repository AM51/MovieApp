package com.example.android.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.movieapp.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ImageAdapter imageAdapter;
    private final String SORT_BY_POPULARITY = "popularity.desc";
    private final String SORT_BY_RATING = "vote_average.desc";
    private final String API_KEY = "eb228d863f83ab41f19e5f698f6a6f2a";
    private boolean favourites=false;
    private String currentPreference=SORT_BY_POPULARITY;
    private String MOVIE_KEY = "saveMovies";

    public MainActivityFragment() {
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(MovieInfo movieInfo);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("archit", "" + imageAdapter.getmThumbIds().size());
        outState.putParcelableArrayList(MOVIE_KEY, (ArrayList<? extends Parcelable>) imageAdapter.getmThumbIds());
        outState.putString("preference", currentPreference);
        outState.putBoolean("favourites", favourites);
        Log.e("archit",currentPreference+" "+favourites);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
//        if(favourites){
//            new FetchFavMovies(getActivity(),imageAdapter,API_KEY).execute();
//        } else {
//            Log.e("archit","inside onstart+ "+currentPreference);
//            new FetchMovieData().execute(currentPreference);
//        }

        Log.e("archit","inside on start");

       // new FetchMovieData().execute(SORT_BY_POPULARITY);

//        if(isNetworkAvailable()) {
//            Log.e("archit","available");
//            if(favourites){
//                new FetchFavMovies(getActivity(),imageAdapter,API_KEY).execute();
//            } else {
//                new FetchMovieData().execute(currentPreference);
//            }
//        } else {
//            Log.e("archit","not available");
//            new FetchFavMoviesFromDb().execute();
//        }

        if(favourites) {
            Log.e("archit","available");
            if(isNetworkAvailable()){
                new FetchFavMovies(getActivity(),imageAdapter,API_KEY).execute();
            } else {
                new FetchFavMoviesFromDb().execute();
            }
        } else {
            Log.e("archit","not available");
            new FetchMovieData().execute(currentPreference);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("archit","inside oncreate view");
        imageAdapter=new ImageAdapter(getActivity());

        if (savedInstanceState != null)
        {
            ArrayList list = (ArrayList<MovieInfo>)savedInstanceState.get(MOVIE_KEY);
            imageAdapter.setmThumbIds(list);
            imageAdapter.notifyDataSetChanged();
            currentPreference=savedInstanceState.getString("preference");
            favourites=savedInstanceState.getBoolean("favourites");
        } else {
            currentPreference = SORT_BY_POPULARITY;
            favourites = false;
        }

        View rootView= inflater.inflate(R.layout.fragment_main, container, false);


        GridView gridView = (GridView)rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfo movieInfo = (MovieInfo) parent.getAdapter().getItem(position);
                ((Callback)getActivity()).onItemSelected(movieInfo);
            }
        });

//        Log.e("archit", "abc");
//        if(favourites) {
//            Log.e("archit","available");
//            if(isNetworkAvailable()){
//                new FetchFavMovies(getActivity(),imageAdapter,API_KEY).execute();
//            } else {
//                new FetchFavMoviesFromDb().execute();
//
//            }
//        } else {
//            Log.e("archit","not available");
//            new FetchMovieData().execute(currentPreference);
//        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.sort_by_pop){
            favourites=false;
            new FetchMovieData().execute(SORT_BY_POPULARITY);
            currentPreference = SORT_BY_POPULARITY;
        } else if(item.getItemId() == R.id.sort_by_rating){
            favourites=false;
            new FetchMovieData().execute(SORT_BY_RATING);
            currentPreference = SORT_BY_RATING;
        } else if(item.getItemId() == R.id.favMovies){
            favourites = true;
            new FetchFavMovies(getActivity(),imageAdapter,API_KEY).execute();
        } else if(item.getItemId() == R.id.refresh){
            new FetchMovieData().execute(currentPreference);
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieData extends AsyncTask<String, Void, ArrayList<MovieInfo>> {

        private final String TAG =  getClass().getName();

        @Override
        protected ArrayList<MovieInfo> doInBackground(String... params) {

            Log.e("archit","inside fetch movie data");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieInfo = null;

            Log.e("archit","point1");

            Uri.Builder builder=new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by", params[0])
                    .appendQueryParameter("api_key", API_KEY);


            try {
                Log.e("archit","point2");
                URL url = new URL(builder.build().toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                Log.e("archit", "point3");

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.e("archit","null stream");
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
                Log.e("archit", "movie info: "+movieInfo);

                return getMovieInfo(movieInfo);
            } catch (IOException e) {
                Log.e("archit", "Errorr ", e);
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return new ArrayList();
        }

        private ArrayList<MovieInfo> getMovieInfo(String movieInfo) throws JSONException {
            JSONObject movieJson = new JSONObject(movieInfo);
            JSONArray movieArray = movieJson.getJSONArray("results");
            ArrayList<MovieInfo> arrayList=new ArrayList<MovieInfo>();
            for (int i=0;i<movieArray.length();i++){
                JSONObject movie=movieArray.getJSONObject(i);
                MovieInfo nMovie = new MovieInfo();

                if(MStringUtils.isValid(movie.getString("original_title"))){
                    nMovie.setOriginal_title(movie.getString("original_title"));
                } else {
                    nMovie.setOriginal_title("Not Available At The Moment");
                }

                nMovie.setPoster_path(movie.getString("poster_path"));

                if(MStringUtils.isValid(movie.getString("release_date"))){
                    nMovie.setRelease_date(movie.getString("release_date"));
                } else {
                    nMovie.setRelease_date("Not Available At The Moment");
                }

                if(MStringUtils.isValid(movie.getString("overview"))){
                    nMovie.setOverview(movie.getString("overview"));
                } else {
                    nMovie.setOverview("Not Available At The Moment");
                }

                if(MStringUtils.isValid(movie.getString("original_language"))){
                    nMovie.setLanguage(movie.getString("original_language"));
                } else {
                    nMovie.setLanguage("Not Available At The Moment");
                }

                nMovie.setRating(movie.getDouble("vote_average")/2.0);
                nMovie.setPopularity(movie.getDouble("popularity"));
                nMovie.setMovieId(movie.getLong("id"));
                if (!nMovie.getPoster_path().equals("null")) {
                    arrayList.add(nMovie);
                }
            }
            return arrayList;
        }


        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieList) {
            if (movieList == null) {
                new FetchFavMoviesFromDb().execute();
                return;
            }

//            if(movieList.size()%2==1){
//                movieList.remove(movieList.size()-1);
//            }

            Log.e("archit",""+movieList.size());
            imageAdapter.clear();
            for (MovieInfo movie : movieList) {
                imageAdapter.addMoviePoster(movie);
            }
            imageAdapter.notifyDataSetChanged();
        }
    }

    public class FetchFavMoviesFromDb extends AsyncTask<Void,Void,ArrayList<MovieInfo>> {


        @Override
        protected ArrayList<MovieInfo> doInBackground(Void... params) {
            Cursor cursor=getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
            int iId = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEID);
            int iTitle=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
            int iOverview = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
            int iDate = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_DATE);
            int iLanguage = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_LANGUAGE);
            int iRating = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
            int iPoster= cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
            ArrayList movieList = new ArrayList();

            Log.e("archit","inside from db method");
            while (cursor.moveToNext()){
                Log.e("archit", "iterating on fav movies");
                MovieInfo movieInfo=new MovieInfo();
                movieInfo.setMovieId(cursor.getLong(iId));
                movieInfo.setOriginal_title(cursor.getString(iTitle));
                movieInfo.setOverview(cursor.getString(iOverview));
                movieInfo.setRelease_date(cursor.getString(iDate));
                movieInfo.setLanguage(cursor.getString(iLanguage));
                movieInfo.setRating(cursor.getDouble(iRating));
                movieInfo.setPoster_path(cursor.getString(iPoster));
                movieList.add(movieInfo);
            }
            return movieList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieList) {
            super.onPostExecute(movieList);
            if (movieList == null) {
                return;
            }

            Log.e("archit", "abc " + movieList.size());
            imageAdapter.clear();
            for (MovieInfo movie : movieList) {
                Log.e("archit",""+movie.getRating());
                imageAdapter.addMoviePoster(movie);
            }
            imageAdapter.notifyDataSetChanged();
        }

    }


}
