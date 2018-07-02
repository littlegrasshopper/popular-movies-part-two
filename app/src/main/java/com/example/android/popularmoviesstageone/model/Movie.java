package com.example.android.popularmoviesstageone.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Movie is a template for the items in the data model and also serves as the entity for Room DB.
 */
@Entity(tableName = "favorite_movies")
@Parcel
public class Movie {

    public static final String POSTER_PATH = "https://image.tmdb.org/t/p/w342%s";
    public static final String BACKDROP_PATH = "https://image.tmdb.org/t/p/w780%s";

    // @SerializedName tells Gson matching json/pojo properties
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    @NonNull
    private String id;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @Ignore
    private ArrayList<MovieTrailer> trailers;

    @Ignore
    private ArrayList<MovieReview> reviews;

    public Movie() {}

    @Ignore
    public Movie(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.posterPath = jsonObject.optString("poster_path");
        this.originalTitle = jsonObject.optString("original_title");
        this.overview = jsonObject.optString("overview");
        this.voteAverage = jsonObject.optDouble("vote_average");
        this.releaseDate = jsonObject.optString("release_date");
        this.backdropPath = jsonObject.optString("backdrop_path");
    }

    // Constructor to be used to create a Room entity
    public Movie(String movieId, String posterPath, String originalTitle,
                 String overview, double voteAverage, String releaseDate, String backdropPath) {
        this.id = movieId;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
    }

    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String movieId) {
        this.id = movieId;
    }

    /**
     * Returns a full URL for the poster image
     * @return Full poster image URL
     */
    public String getFullPosterPath() {
        return String.format(POSTER_PATH, this.posterPath);
    }

    /**
     * Returns the original poster path retrieved from The Movie DB API
     * This value will be stored in the Room DB
     * @return Path of original poster
     */
    public String getPosterPath() {
        return this.posterPath;
    }

    public void setPosterPath(String path) {
        this.posterPath = path;
    }

    /**
     * Returns a full URL for the backdrop image
     * @return Full backdrop image URL
     */
    public String getFullBackdropPath() {
        String path = backdropPath;
        if (backdropPath == null || backdropPath.isEmpty() || backdropPath == "null" ) {
            return getFullPosterPath();
        }
        return String.format(BACKDROP_PATH, path);
    }

    /**
     * Returns the original backdrop image path retrieved from The Movie DB API
     * @return Path for original backdrop image
     */
    public String getBackdropPath() {
        String path = backdropPath;
        if (backdropPath == null || backdropPath.isEmpty() || backdropPath == "null" ) {
            return getPosterPath();
        }
        return path;
    }

    public void setBackdropPath(String path) {
        this.backdropPath = path;
    }

    /**
     * Returns the movie title
     * @return String representing movie title
     */
    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public void setOriginalTitle(String title) {
        this.originalTitle = title;
    }

    /**
     * Returns the movie overview
     * @return String respresenting movie overview
     */
    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * Returns the movie release date
     * @return String representing movie release date
     */
    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(String date) {
        this.releaseDate = date;
    }

    /**
     * Returns the movie vote average
     * @return Double representing movie vote average
     */
    public Double getVoteAverage() {
        return this.voteAverage;
    }

    public void setVoteAverage(Double average) {
        this.voteAverage = average;
    }

    public ArrayList<MovieReview> getReviews() {
        return this.reviews;
    }

    public void setReviews(ArrayList<MovieReview> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<MovieTrailer> getTrailers() {
        return this.trailers;
    }

    public void setTrailers(ArrayList<MovieTrailer> trailers) {
        this.trailers = trailers;
    }

    /**
     * Convenience class for returning a list of movie objects
     */
    public static class MovieResult {

        public static String DATE_FORMAT = "dd MMM yyy";

        private ArrayList<Movie> results;

        public MovieResult() {
            results = new ArrayList<>();
        }

        public ArrayList<Movie> getResults() {
            return results;
        }

    }

}
