package com.example.android.popularmoviesstageone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.R;
import com.example.android.popularmoviesstageone.model.MovieReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Movie Review objects.
 * Credit: Udacity S03.01-Exercise-RecyclerView ForecastAdapter.java
 */

public class MovieReviewAdapter extends
        RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    public static final String TAG = MovieReviewAdapter.class.getSimpleName();
    private static final int MAX_REVIEW_LINES = 5;

    private Context mContext;

    private List<MovieReview> mReviews = new ArrayList<>();

    public MovieReviewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View movieReviewView = inflater.inflate(R.layout.item_movie_review, parent, shouldAttachToParentImmediately);
        MovieReviewViewHolder viewHolder = new MovieReviewViewHolder(movieReviewView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        MovieReview review = mReviews.get(position);
        TextView author = (TextView) holder.tvAuthor;
        TextView content = (TextView) holder.tvContent;
        TextView reviewUrl = (TextView) holder.tvReviewUrl;

        Log.d(TAG, "OnBindViewHolder has " + review.getReviewUrl());
        author.setText(review.getAuthor());
        content.setMaxLines(MAX_REVIEW_LINES);
        content.setEllipsize(TextUtils.TruncateAt.END);
        content.setText(review.getContent());

        // Set up the 'see more' link to see rest of the review content
        // Credits:
        //https://stackoverflow.com/questions/3656371/dynamic-string-using-string-xml
        //https://guides.codepath.com/android/Working-with-the-TextView
        //https://stackoverflow.com/questions/9204303/android-is-it-possible-to-add-a-clickable-link-into-a-string-resource
        //http://www.aviyehuda.com/blog/2011/01/27/android-creating-links-using-linkfy/
        //https://developer.android.com/guide/topics/resources/string-resource#FormattingAndStyling
        // Notes:
        // Tried Linkify but was unsuccessful, needs more research.
        // Underline for link still showing

        String seeMoreLink =
                mContext.getString(R.string.see_more, review.getReviewUrl());
        reviewUrl.setMovementMethod(LinkMovementMethod.getInstance());
        reviewUrl.setText(Html.fromHtml(seeMoreLink));
    }

    @Override
    public int getItemCount() {
        return (mReviews == null ? 0 : mReviews.size());
    }

    /**
     * ViewHolder class
     */
    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        /* ButterKnife binding */
        @BindView(R.id.tvAuthor) TextView tvAuthor;
        @BindView(R.id.tvContent) TextView tvContent;
        @BindView(R.id.tvReviewUrl) TextView tvReviewUrl;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setMovieReviewData(ArrayList<MovieReview> reviewData) {
        mReviews.clear();
        mReviews.addAll(reviewData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}