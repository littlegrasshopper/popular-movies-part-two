package com.example.android.popularmoviesstageone.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmoviesstageone.database.AppDatabase;

/**
 * Created by fonda on 6/19/18.
 */

public class AddDeleteFavoritesViewModel extends ViewModel {

    private LiveData<Movie> favorite;

    public AddDeleteFavoritesViewModel(AppDatabase database, String movie_id) {
        favorite = database.favoritesDao().loadFavoritesById(movie_id);
    }

    public LiveData<Movie> getFavorite() {
        return favorite;
    }
}
