package com.example.android.popularmoviesstageone.service;

import android.database.Observable;

import com.example.android.popularmoviesstageone.model.Movie;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbService {
    @GET("/3/movie/{category}?")
    Observable<Movie.MovieResult> getMovies (
            @Path("category") String category,
            @Query("api_key") String key);
}