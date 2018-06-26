package com.example.android.popularmoviesstageone.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.popularmoviesstageone.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fonda on 6/18/18.
 */

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorite_movies ORDER BY movie_id")
    LiveData<List<Movie>> loadAllFavoriteMovies();

    @Insert
    void insertFavorites(Movie movie);

    @Delete
    void deleteFavorites(Movie movie);

    @Query("SELECT * FROM favorite_movies WHERE movie_id = :movie_id")
    LiveData<Movie> loadFavoritesById(String movie_id);
}
