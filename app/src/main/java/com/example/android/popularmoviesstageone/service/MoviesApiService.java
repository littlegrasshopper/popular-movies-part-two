package com.example.android.popularmoviesstageone.service;

import com.example.android.popularmoviesstageone.model.Movie;
import com.example.android.popularmoviesstageone.model.MovieReview;
import com.example.android.popularmoviesstageone.model.MovieTrailer;

import retrofit2.http.GET;
import retrofit2.http.Path;


public interface MoviesApiService {
    @GET("{category}?")
    rx.Observable<Movie.MovieResult> getMovies (
            @Path("category") String category);

    @GET("{id}/videos?")
    rx.Observable<MovieTrailer.TrailerResult> getTrailers (
            @Path("id") String id);

    @GET("{id}/reviews?")
    rx.Observable<MovieReview.ReviewResult> getReviews (
            @Path("id") String id);

    @GET("{id}/mqdefault.jpg")
    rx.Observable<MovieTrailer> getTrailer (
            @Path("id") String id);
}
