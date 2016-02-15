package com.example.android.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by archit.m on 02/01/16.
 */
public class ReviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovieReview> movieReviews;

    public ReviewAdapter(Context context, ArrayList<MovieReview> movieReviews) {
        this.context = context;
        this.movieReviews = movieReviews;
    }

    public ArrayList<MovieReview> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(ArrayList<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    @Override
    public int getCount() {
        return movieReviews.size();
    }

    @Override
    public MovieReview getItem(int position) {
        return movieReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ReviewHolder holder = new ReviewHolder();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.review_row_layout, null);
            TextView author = (TextView) v.findViewById(R.id.author);
            TextView content = (TextView) v.findViewById(R.id.content);

            holder.author = author;
            holder.content = content;

            v.setTag(holder);
        }
        else
            holder = (ReviewHolder) v.getTag();

        holder.author.setText(movieReviews.get(position).getAuthor());
        holder.content.setText(movieReviews.get(position).getContent());

        return v;
    }
}
