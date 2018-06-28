package com.example.android.popularmoviesstageone.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.R;
import com.example.android.popularmoviesstageone.model.MovieReview;
import com.example.android.popularmoviesstageone.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Movie Trailer objects.
 * Credit: Udacity S03.01-Exercise-RecyclerView ForecastAdapter.java
 */

public class MovieTrailerAdapter extends
        RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {

    public static final String TAG = MovieTrailerAdapter.class.getSimpleName();

    //public interface MovieReviewAdapterOnClickHandler {
    //void onClick(Movie m);
    //}

    private Context mContext;

    //private final MovieReviewAdapterOnClickHandler mClickHandler;

    private List<MovieTrailer> mTrailers = new ArrayList<>();

    /*
    public MovieTrailerAdapter(Context context, MovieTrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }
    */

    public MovieTrailerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View movieTrailerView = inflater.inflate(R.layout.item_movie_trailer, parent, shouldAttachToParentImmediately);
        MovieTrailerViewHolder viewHolder = new MovieTrailerViewHolder(movieTrailerView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        MovieTrailer trailer = mTrailers.get(position);
        ImageView movieTrailer = (ImageView) holder.ivTrailer;
        Picasso.with(mContext).load(trailer.getTrailerName()).into(movieTrailer); // TODO Fix this
    }

    @Override
    public int getItemCount() {
        return (mTrailers == null ? 0 : mTrailers.size());
    }

    /**
     * ViewHolder class
     */
    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {

        /* ButterKnife binding */
        @BindView(R.id.ivMovieTrailer) ImageView ivTrailer;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //itemView.setOnClickListener(this);
        }

        /*
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieReview review = mReviews.get(adapterPosition);
            mClickHandler.onClick(review);
        }
        */
    }

    public void setMovieTrailerData(ArrayList<MovieTrailer> trailerData) {
        mTrailers.clear();
        mTrailers.addAll(trailerData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
