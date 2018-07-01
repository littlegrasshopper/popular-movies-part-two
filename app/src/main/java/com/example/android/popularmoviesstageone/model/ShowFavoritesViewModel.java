package com.example.android.popularmoviesstageone.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.popularmoviesstageone.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for displaying user's list of favorite movies.
 * Credit: Lesson 12 Android Architecture Components
 * Credit: Lesson T09b.10-Exercise-AddViewModelToAddTaskActivity
 */
public class ShowFavoritesViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = ShowFavoritesViewModel.class.getSimpleName();

    private LiveData<List<Movie>> favorites;

    public ShowFavoritesViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving all favorites from the database");
        favorites = database.favoritesDao().loadAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavorites() {
        return favorites;
    }
}
