package com.example.android.popularmoviesstageone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.R;
import com.example.android.popularmoviesstageone.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Movies objects.
 * Credit: Udacity S03.01-Exercise-RecyclerView ForecastAdapter.java
 */

public class MovieArrayAdapter extends
        RecyclerView.Adapter<MovieArrayAdapter.MovieViewHolder> {

    public static final String TAG = MovieArrayAdapter.class.getSimpleName();

    public interface MovieArrayAdapterOnClickHandler {
        void onClick(Movie m);
    }

    private Context mContext;

    private final MovieArrayAdapterOnClickHandler mClickHandler;

    private List<Movie> mMovies;

    public MovieArrayAdapter(Context context, MovieArrayAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View movieView = inflater.inflate(R.layout.item_movie, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(movieView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
       ImageView imageView = (ImageView) holder.ivImage;
       Picasso.with(mContext).load(movie.getPosterPath()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return (mMovies == null ? 0 : mMovies.size());
    }

    /**
     * ViewHolder class
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ButterKnife binding */
        @BindView(R.id.ivMovieImage) ImageView ivImage;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    public void setMovieData(ArrayList<Movie> movieData) {
        mMovies = movieData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
