package com.example.android.popularmoviesstageone.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmoviesstageone.database.AppDatabase;

/**
 * ViewModel for tracking favorite movies
 * Credit: Lesson 12 Android Architecture Components
 * Credit: Lesson T09b.10-Exercise-AddViewModelToAddTaskActivity
 */
public class AddDeleteFavoritesViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = AddDeleteFavoritesViewModel.class.getSimpleName();

    private LiveData<Movie> favorite;

    public AddDeleteFavoritesViewModel(AppDatabase database, String movie_id) {
        Log.d(TAG, "Actively retrieving single favorite from the database: " + movie_id);
        favorite = database.favoritesDao().loadFavoritesById(movie_id);
    }

    public LiveData<Movie> getFavorite() {
        return favorite;
    }
}
