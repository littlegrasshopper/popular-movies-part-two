package com.example.android.popularmoviesstageone.model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.android.popularmoviesstageone.database.AppDatabase;

/**
 * Created by fonda on 6/19/18.
 */
// TODO Should use movieID or favoritesID?

public class FavoritesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final String mMovieId;

    public FavoritesViewModelFactory(AppDatabase database, String movieId) {
        mDb = database;
        mMovieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddDeleteFavoritesViewModel(mDb, mMovieId);
    }
}
