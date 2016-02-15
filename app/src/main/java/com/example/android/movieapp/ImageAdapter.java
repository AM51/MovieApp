package com.example.android.movieapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by archit.m on 25/11/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final String BASE_URL="http://image.tmdb.org/t/p/w185/";

    public ArrayList<MovieInfo> mThumbIds;

    public ImageAdapter(Context c){
        mContext = c;
        mThumbIds=new ArrayList();
    }

    public ArrayList<MovieInfo> getmThumbIds() {
        return mThumbIds;
    }

    public void setmThumbIds(ArrayList<MovieInfo> mThumbIds) {
        this.mThumbIds = mThumbIds;
    }

    public void addMoviePoster(MovieInfo movieInfo){
        mThumbIds.add(movieInfo);
    }
    @Override
    public int getCount() {
        return mThumbIds.size();
    }

    @Override
    public MovieInfo getItem(int position) {
        return mThumbIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        Picasso.with(mContext).load(BASE_URL+getItem(position).getPoster_path()).resize(400,400).into(imageView);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

    public void clear(){
        mThumbIds.clear();
    }
}
