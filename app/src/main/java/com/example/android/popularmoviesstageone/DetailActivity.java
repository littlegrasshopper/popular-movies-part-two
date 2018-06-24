package com.example.android.popularmoviesstageone;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmoviesstageone.database.AppDatabase;
import com.example.android.popularmoviesstageone.database.FavoritesEntry;
import com.example.android.popularmoviesstageone.model.AddDeleteFavoritesViewModel;
import com.example.android.popularmoviesstageone.model.FavoritesViewModelFactory;
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
    // Constant for logging
    private static final String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.ivMovieImage) ImageView mImage;
    @BindView(R.id.tvOverview) TextView mOverview;
    @BindView(R.id.tvOriginalTitle) TextView originalTitle;
    @BindView(R.id.tvReleaseDate) TextView mReleaseDate;
    @BindView(R.id.rbRating) RatingBar mRating;
    @BindView(R.id.tbFavorite)
    ToggleButton mFavorite;
    @BindView(R.id.detailToolbar) android.support.v7.widget.Toolbar mToolbar;

    // Extra for the movie ID to be received in the intent
    public static final String EXTRA_MOVIE_ID = "extraMovieId";
    // Extra for the movie ID to be received after rotation
    public static final String INSTANCE_MOVIE_ID = "instanceMovieId";

    // Constant for default entry id to be used when not in update mode
    private static final String DEFAULT_MOVIE_ID = "-1";

    private AppDatabase mDb;
    private String mMovieId = DEFAULT_MOVIE_ID;
    private Movie movie;

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

        mDb = AppDatabase.getsInstance(getApplicationContext());


        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE_ID)) {
            mMovieId = savedInstanceState.getString(INSTANCE_MOVIE_ID, DEFAULT_MOVIE_ID);
        }

        // Get the passed in intent
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Movie.MOVIE_EXTRA)) {
            movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.MOVIE_EXTRA));

            mMovieId = movie.getId();
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

            // Update the favorite status if needed
            //AppExecutors.getInstance().diskIO().execute(new Runnable() {
               // @Override
                //public void run() {
            /*
                    final LiveData<Movie> favoriteMovie = mDb.favoritesDao().loadFavoritesById(mMovieId);
                    favoriteMovie.observe(this, new Observer<Movie>() {
                        @Override
                        public void onChanged(@Nullable Movie movie) {
                            populateUI(favoriteMovie);
                            Log.d(TAG, "Receiving database update for single favorite movie");
                            favoriteMovie.removeObserver(this); // ?????
                        }
                    });
                    */
                    //runOnUiThread(new Runnable() {
                        //@Override
                        //public void run() {
                            //Toast.makeText(getApplicationContext(), "Hello I am in runonuithread", Toast.LENGTH_SHORT);
                            //populateUI(favoriteMovie);

                        //}
                    //});
                //}
            //});

            // Credit: Udacity Lesson 12 Android Architecture Components
            // === Start ===
            FavoritesViewModelFactory factory = new FavoritesViewModelFactory(mDb, mMovieId);
            final AddDeleteFavoritesViewModel model = ViewModelProviders
                    .of(this, factory)
                    .get(AddDeleteFavoritesViewModel.class);
            model.getFavorite().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable final Movie movie) {
                    model.getFavorite().removeObserver(this);
                    Log.d(TAG, "Receiving database update from LiveData");
                    populateUI(movie);
                }
            });
            // === End ===

            mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    final boolean isChecked = checked;
                    Log.d(TAG, "check changed: " + isChecked);
                    final boolean existsInDB = isFavorite();

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (isChecked) {
                                if (!existsInDB) {
                                    mDb.favoritesDao().insertFavorites(movie);
                                }
                            } else {
                                if (existsInDB) {
                                    mDb.favoritesDao().deleteFavorites(movie);
                                }
                            }
                        }
                    });


                    /*
                    if (checked) {
                        Log.d(TAG, "CHECKED");
                        mFavorite.setBackgroundResource(R.drawable.ic_star_black_24dp);
                        //TODO Add the movie favorites entry
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                // Check to see if the movie already exists in the DB
                                final LiveData<Movie> favoriteMovie =
                                        mDb.favoritesDao().loadFavoritesById(mMovieId);
                                // Add an entry to the favorites DB
                                if (favoriteMovie == null) {
                                    mDb.favoritesDao().insertFavorites(movie);
                                }
                            }
                        });

                    }
                    else {
                        Log.d(TAG, "UNCHECKED");
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.favoritesDao().deleteFavorites(movie);
                            }
                        });
                        mFavorite.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                    }*/
                }
            });




        }
    }

    private boolean isFavorite() {
        boolean isFavorite = false;
        FavoritesViewModelFactory factory = new FavoritesViewModelFactory(mDb, mMovieId);
        final AddDeleteFavoritesViewModel model = ViewModelProviders
                .of(this, factory)
                .get(AddDeleteFavoritesViewModel.class);
        isFavorite = model.getFavorite() != null && (model.getFavorite().getValue() != null);
        return isFavorite;
    }


    private void populateUI(Movie fav) {
        if (fav == null) {
            Log.i(TAG, "Populate UI favoirte is null");
            mFavorite.setChecked(false);
        } else {
            Log.i(TAG, "Populate UI favorite exists");
            mFavorite.setChecked(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Movie.MOVIE_EXTRA, Parcels.wrap(movie));
        super.onSaveInstanceState(outState);
    }
}