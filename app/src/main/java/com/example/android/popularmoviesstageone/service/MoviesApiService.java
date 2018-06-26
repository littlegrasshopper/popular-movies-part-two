package com.example.android.popularmoviesstageone.service;

import android.database.Observable;

import com.example.android.popularmoviesstageone.model.Movie;
import com.example.android.popularmoviesstageone.model.MovieReview;
import com.example.android.popularmoviesstageone.model.MovieTrailer;

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

    @GET("{id}/videos?")
    rx.Observable<MovieTrailer> getTrailers (
            @Path("id") String id);

    @GET("{id}/reviews?")
    rx.Observable<MovieReview> getReviews (
            @Path("id") String id);

}
