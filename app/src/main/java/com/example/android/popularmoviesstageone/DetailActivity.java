package com.example.android.popularmoviesstageone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.model.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Activity for displaying details of a movie.
 */
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.ivMovieImage) ImageView mImage;
    @BindView(R.id.tvOverview) TextView mOverview;
    @BindView(R.id.tvOriginalTitle) TextView originalTitle;
    @BindView(R.id.tvReleaseDate) TextView mReleaseDate;
    @BindView(R.id.rbRating) RatingBar mRating;
    @BindView(R.id.detailToolbar) android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // Enable up icon
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get the passed in intent
        Intent intent = getIntent();

        if (intent.hasExtra(Movie.MOVIE_EXTRA)) {
            Movie movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.MOVIE_EXTRA));

            mOverview.setText(movie.getOverview());
            originalTitle.setText(movie.getOriginalTitle());

            // Credit: https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library
            // === Start ===
            Picasso.with(this).load(movie.getBackdropPath()).fit().centerCrop()
                .transform(new RoundedCornersTransformation(10,10))
                .into(mImage);
            // === End ===

            // Format date for pretty display
            // Credit: https://stackoverflow.com/questions/35939337/how-to-convert-date-in-particular-format-in-android
            SimpleDateFormat sdfOriginalPattern = new SimpleDateFormat("yyyy-mm-dd");
            try {
                Date relDate = sdfOriginalPattern.parse(movie.getReleaseDate());
                SimpleDateFormat sdfRevisedPattern = new SimpleDateFormat("dd MMM yyy");
                mReleaseDate.setText(sdfRevisedPattern.format(relDate));

            } catch (ParseException e) {
                mReleaseDate.setText(movie.getReleaseDate());
            }

            // To get the smaller mRating bar, set styles configuration in activity_detail.xml
            // Credit: https://stackoverflow.com/questions/2874537/how-to-make-a-smaller-ratingbar
            mRating.setRating((int) Math.round(movie.getVoteAverage()));
        }
    }
}