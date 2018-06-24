package com.example.android.popularmoviesstageone.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmoviesstageone.database.AppDatabase;
import com.example.android.popularmoviesstageone.database.FavoritesEntry;

/**
 * Created by fonda on 6/19/18.
 */

public class AddDeleteFavoritesViewModel extends ViewModel {

    private LiveData<Movie> favorite;

    public AddDeleteFavoritesViewModel(AppDatabase database, String movieId) {
        favorite = database.favoritesDao().loadFavoritesById(movieId);
    }

    public LiveData<Movie> getFavorite() {
        return favorite;
    }
}
