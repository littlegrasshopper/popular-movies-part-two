package com.example.android.popularmoviesstageone;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.adapter.MovieArrayAdapter;
import com.example.android.popularmoviesstageone.database.AppDatabase;
import com.example.android.popularmoviesstageone.model.Movie;
import com.example.android.popularmoviesstageone.model.ShowFavoritesViewModel;
import com.example.android.popularmoviesstageone.utilities.MovieJsonUtils;
import com.example.android.popularmoviesstageone.utilities.NetworkUtils;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpException;
import okhttp3.OkHttpClient;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Main activity to display a grid of movie poster images.
 * Credit: Udacity S03.01-Exercise-RecyclerView (MainActivity.java)
 */
public class MovieActivity extends AppCompatActivity
        implements MovieArrayAdapter.MovieArrayAdapterOnClickHandler {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private MovieArrayAdapter mMovieAdapter;
    private Subscription subscription;
    private AppDatabase mDb;

    @BindView(R.id.rvMovies) RecyclerView mMoviesRecyclerView;
    @BindView(R.id.tbToolbar) android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.spSortBy) Spinner mSpinner;
    @BindView(R.id.tvErrorMessage) TextView mErrorMessageDisplay;
    @BindView(R.id.pbLoadingIndicator) ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        // Sets the mToolbar to act as the ActionBar for this Activity window.
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setupMovies();
        setupFilter();
        fetchMovieData(NetworkUtils.getFilterType(0));
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * Setup the movies recycler view
     */
    private void setupMovies() {
        mMovieAdapter = new MovieArrayAdapter(this, this);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this,
                3,
                GridLayoutManager.VERTICAL,
                false
        );
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * Set up the filter/sort by
     */
    private void setupFilter() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selected = adapterView.getSelectedItemPosition();
                fetchMovieData(NetworkUtils.getFilterType(selected));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Fetch the movies based on the sort filter
     * @param sortBy The filter to use to retrieve the movies.
     */
    private void fetchMovieData(String sortBy ) {
        if (isOnline()) {
            showMovieDataView();
            //new FetchMoviesDataTask().execute(sortBy);
            getMovies(sortBy);
        } else {
            showErrorMessage();
        }
    }

    /**
     * RxJava call to retrieve the list of movies asynchronously
     * @param category
     */
    private void getMovies(String category) {
        if (category == NetworkUtils.FAVORITES) {
            setupViewModel();
            return;
        }
        subscription = MovieClient.getInstance()
                .getMovies(category)
                // scheduler where the Observable will do the work
                .subscribeOn(Schedulers.io())
                // scheduler which a subscriber will observe this Observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie.MovieResult>() {
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
                    public void onNext(Movie.MovieResult movieResults) {
                        Log.d(TAG, "OnNext");
                        Log.d(TAG, "movie results are: " + movieResults);
                        mMovieAdapter.setMovieData(movieResults.getResults());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setupViewModel();
    }

    /**
     * Setup the ViewModel for the favorite movies
     */
    private void setupViewModel() {
        mDb = AppDatabase.getsInstance(getApplicationContext());
        ShowFavoritesViewModel viewModel = ViewModelProviders
                .of(this)
                .get(ShowFavoritesViewModel.class);

        viewModel.getFavorites()
                .observe(this, new android.arch.lifecycle.Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> favoritesEntries) {
                Log.d(TAG, "Updating list of favorites from LiveData in ViewModel: " + favoritesEntries);
                // Lesson 12.13 4:29 This needs to be run on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMovieAdapter.setMovieData((ArrayList<Movie>)favoritesEntries);
                    }
                });
            }
        });
    }

    /**
     * Show the view for the movies data and hide the error message display.
     */
    private void showMovieDataView() {
        // hide the error message display
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // show the list of movies
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show the error message display and hide the movies data.
     */
    private void showErrorMessage() {
        // hide the view for the list of movies
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        // show the error message
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * Check to make sure there is network connection
     * @return True if network is available, false otherwise.
     * Credit:
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */
    // === Start ===
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    // === End ===

    @Override
    public void onClick(Movie movie) {
        Class destinationActivity = DetailActivity.class;
        Context context = MovieActivity.this;
        Intent intent = new Intent(context, destinationActivity);

        intent.putExtra(DetailActivity.EXTRA_MOVIE, Parcels.wrap(movie));
        startActivity(intent);
    }
}
