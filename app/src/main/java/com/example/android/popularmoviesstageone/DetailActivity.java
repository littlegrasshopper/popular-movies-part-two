package com.example.android.popularmoviesstageone;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.popularmoviesstageone.adapter.MovieArrayAdapter;
import com.example.android.popularmoviesstageone.adapter.MovieReviewAdapter;
import com.example.android.popularmoviesstageone.adapter.MovieTrailerAdapter;
import com.example.android.popularmoviesstageone.database.AppDatabase;
import com.example.android.popularmoviesstageone.model.AddDeleteFavoritesViewModel;
import com.example.android.popularmoviesstageone.model.FavoritesViewModelFactory;
import com.example.android.popularmoviesstageone.model.Movie;
import com.example.android.popularmoviesstageone.model.MovieReview;
import com.example.android.popularmoviesstageone.model.MovieTrailer;
import com.example.android.popularmoviesstageone.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpException;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Activity for displaying details of a movie.
 */
public class DetailActivity extends AppCompatActivity
        implements MovieTrailerAdapter.MovieTrailerAdapterOnClickHandler {
    // Constant for logging
    private static final String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.ivMovieImage) ImageView mImage;
    @BindView(R.id.tvOverview) TextView mOverview;
    @BindView(R.id.tvOriginalTitle) TextView originalTitle;
    @BindView(R.id.tvReleaseDate) TextView mReleaseDate;
    @BindView(R.id.rbRating) RatingBar mRating;
    @BindView(R.id.tbFavorite) ToggleButton mFavorite;
    @BindView(R.id.movieReview) View mReviews;
    @BindView(R.id.movieTrailer) View mTrailers;
    @BindView(R.id.detailToolbar) android.support.v7.widget.Toolbar mToolbar;

    @BindView(R.id.activity_detail) ScrollView mScrollView;
    @BindView(R.id.rvReviews) RecyclerView mReviewsRecyclerView;
    @BindView(R.id.rvTrailers) RecyclerView mTrailersRecyclerView;

    // Extra for the movie ID to be received in the intent
    public static final String EXTRA_MOVIE = "extraMovie";
    // Extra for the movie ID to be received after rotation
    public static final String INSTANCE_MOVIE = "instanceMovie";

    private AppDatabase mDb;
    private String mMovieId;
    private Movie movie;

    // Movie Reviews
    private Subscription subscriptionReviews;
    private MovieReviewAdapter mMovieReviewAdapter;

    // Movie Trailers
    private Subscription subscriptionTrailers;
    private MovieTrailerAdapter mMovieTrailerAdapter;


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

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE)) {
            movie = Parcels.unwrap(savedInstanceState.getParcelable(INSTANCE_MOVIE));
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("ARTICLE_SCROLL_POSITION"))
        {

        }

        // Get the passed in intent
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_MOVIE)) {
            if (movie == null) {
                movie = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_MOVIE));
            }

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

            // Update the favorite status
            // Credit: Udacity Lesson 12 Android Architecture Components
            // === Start ===
            FavoritesViewModelFactory factory = new FavoritesViewModelFactory(mDb, mMovieId);
            final AddDeleteFavoritesViewModel model = ViewModelProviders
                    .of(this, factory)
                    .get(AddDeleteFavoritesViewModel.class);
            model.getFavorite().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable final Movie movie) {
                    // Don't remove the observer
                    //model.getFavorite().removeObserver(this);
                    Log.d(TAG, "Receiving database update from LiveData");
                    populateUI(movie);
                }
            });
            // === End ===

            // Listen for user tapping on favorite button
            mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    boolean isChecked = checked;
                    Log.d(TAG, "check changed: " + isChecked);
                    boolean existsInDB = isFavorite();

                    FavoritesViewModelFactory factory = new FavoritesViewModelFactory(mDb, mMovieId);
                    Log.d(TAG, "isFavorite(): movie_id: " + mMovieId);
                    /*
                    AddDeleteFavoritesViewModel model = ViewModelProviders
                            .of(DetailActivity.this, factory)
                            .get(AddDeleteFavoritesViewModel.class);
                    existsInDB = model.getFavorite() != null && (model.getFavorite().getValue() != null);
                    */

                    Log.d(TAG, "exists in DB: " + existsInDB);

                    if (isChecked) {
                        if (!existsInDB) {
                            Log.d(TAG, "doesn't exist in DB, adding");
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mDb.favoritesDao().insertFavorites(movie);
                                }
                            });
                        }
                    } else {
                        if (existsInDB) {
                            Log.d(TAG, "exists in DB, deleting");
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mDb.favoritesDao().deleteFavorites(movie);
                                }
                            });
                        }
                    }


                    /*
                    if (checked) {
                        Log.d(TAG, "CHECKED");
                        mFavorite.setBackgroundResource(R.drawable.ic_star_white_24dp);
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
                        mFavorite.setBackgroundResource(R.drawable.ic_star_border_white_24dp);
                    }*/
                }
            });

            setupReviews();
            setupTrailers();
            fetchReviews(mMovieId);
            fetchTrailers(mMovieId);

        }
    }

    private void setupReviews() {
        mMovieReviewAdapter = new MovieReviewAdapter(this);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setAdapter(mMovieReviewAdapter);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mReviewsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupTrailers() {
        mMovieTrailerAdapter = new MovieTrailerAdapter(this, this);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setAdapter(mMovieTrailerAdapter);
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mTrailersRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * RxJava call to retrieve the list of reviews asynchronously
     * @param movieId
     */
    private void fetchReviews(String movieId) {

        //mReviewsRecyclerView.setVisibility(View.GONE);

        subscriptionReviews = MovieClient.getInstance()
                .getReviews(movieId)
                // scheduler where the Observable will do the work
                .subscribeOn(Schedulers.io())
                // scheduler which a subscriber will observe this Observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Observer<MovieReview.ReviewResult>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        // cast to retrofit2.HttpException to get the response code
                        if (e instanceof HttpException) {
                            int code = ((retrofit2.HttpException)e).code();
                        }
                        Log.d(TAG, "In Error");
                    }

                    @Override
                    public void onNext(MovieReview.ReviewResult reviewResults) {
                        Log.d(TAG, "OnNext");
                        Log.d(TAG, "movie reviews are: " + reviewResults.getResults());
                        if (reviewResults.getResults().size() > 0) {
                            mReviews.setVisibility(View.VISIBLE);
                            mMovieReviewAdapter.setMovieReviewData(reviewResults.getResults());
                        }

                    }
                });
    }

    /**
     * RxJava call to retrieve the list of reviews asynchronously
     * @param movieId
     */
    private void fetchTrailers(String movieId) {

        //mReviewsRecyclerView.setVisibility(View.GONE);

        subscriptionTrailers = MovieClient.getInstance()
                .getTrailers(movieId)
                // scheduler where the Observable will do the work
                .subscribeOn(Schedulers.io())
                // scheduler which a subscriber will observe this Observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Observer<MovieTrailer.TrailerResult>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        // cast to retrofit2.HttpException to get the response code
                        if (e instanceof HttpException) {
                            int code = ((retrofit2.HttpException)e).code();
                        }
                        Log.d(TAG, "In Error");
                    }

                    @Override
                    public void onNext(MovieTrailer.TrailerResult trailerResults) {
                        Log.d(TAG, "OnNext");
                        Log.d(TAG, "movie trailers are: " + trailerResults.getResults());
                        if (trailerResults.getResults().size() > 0) {
                            mTrailers.setVisibility(View.VISIBLE);
                            mMovieTrailerAdapter.setMovieTrailerData(trailerResults.getResults());
                        }

                    }
                });
    }


        @Override
    protected void onDestroy() {
        if (subscriptionReviews != null && !subscriptionReviews.isUnsubscribed()) {
            subscriptionReviews.unsubscribe();
        }
            if (subscriptionTrailers != null && !subscriptionTrailers.isUnsubscribed()) {
                subscriptionTrailers.unsubscribe();
            }
        super.onDestroy();
    }

    private boolean isFavorite() {
        boolean isFavorite = false;
        FavoritesViewModelFactory factory = new FavoritesViewModelFactory(mDb, mMovieId);
        Log.d(TAG, "isFavorite(): movie_id: " + mMovieId);
        AddDeleteFavoritesViewModel model = ViewModelProviders
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

    //TODO Display trailers
    /*
    Intent webIntent = new Intent(Intent.ACTION_VIEW, trailerWebpage);
     */
    //TODO: use implicit intent?


    //Credit: https://stackoverflow.com/questions/17877595/i-want-text-view-as-a-clickable-link

    @Override
    public void onClick(MovieTrailer m) {
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
        trailerIntent.setData(Uri.parse(MovieTrailer.TRAILER_BASE_URL + m.getKey()));
        startActivity(trailerIntent);
    }

    // Saving scroll position
    // Credit: https://stackoverflow.com/questions/29208086/save-the-position-of-scrollview-when-the-orientation-changes
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INSTANCE_MOVIE, Parcels.wrap(movie));
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if(position != null)
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(position[0], position[1]);
                }
            });
    }
}