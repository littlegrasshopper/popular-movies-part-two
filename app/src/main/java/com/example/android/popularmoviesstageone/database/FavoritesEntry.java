package com.example.android.popularmoviesstageone.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by fonda on 6/18/18.
 */

@Entity(tableName = "favorites")
public class FavoritesEntry {

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "movie_title")
    private String movieTitle;

    // Constructor to be used to create an entity
    public FavoritesEntry(int movieId, String movieTitle) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    // Constructor to be used by Room for existing entities
    /*
    public FavoritesEntry(int id, int movieId, String movieTitle) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }*/

   /* public int getId() { return id; }

    public void setId(int id) { this.id = id; }*/

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}
