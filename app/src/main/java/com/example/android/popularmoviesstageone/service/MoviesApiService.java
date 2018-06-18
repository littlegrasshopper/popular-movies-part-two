package com.example.android.popularmoviesstageone.service;

import android.database.Observable;

import com.example.android.popularmoviesstageone.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
public interface MoviesApiService {
    @GET("/movie/popular")
    void getPopularMovies(Callback<Movie.MovieResult> cb);
}
*/

public interface MoviesApiService {
    @GET("{category}?")
    rx.Observable<Movie.MovieResult> getMovies (
            @Path("category") String category);
}
