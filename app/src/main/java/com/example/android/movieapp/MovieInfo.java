package com.example.android.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by archit.m on 25/11/15.
 */
public class MovieInfo implements Parcelable {

    private String original_title;

    private String release_date;

    private String poster_path;

    private String overview;

    private String language;

    private Double rating;

    private Double popularity;

    private Long movieId;

    public MovieInfo() {
    }

    protected MovieInfo(Parcel in) {
        original_title=in.readString();
        release_date=in.readString();
        poster_path=in.readString();
        overview=in.readString();
        language=in.readString();
        rating=in.readDouble();
        //popularity=in.readDouble();
        movieId=in.readLong();
    }


    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(language);
        dest.writeDouble(rating);
        //if(popularity!=null)dest.writeDouble(popularity);
        dest.writeLong(movieId);
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }


    };
}
