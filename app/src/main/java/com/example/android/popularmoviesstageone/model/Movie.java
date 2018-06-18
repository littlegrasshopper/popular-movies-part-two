package com.example.android.popularmoviesstageone.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Movie is a template for the items in the data model.
 */
@Parcel
public class Movie {

    public static final String MOVIE_EXTRA = "movie";
    public static final String POSTER_PATH = "https://image.tmdb.org/t/p/w342/%s";
    public static final String BACKDROP_PATH = "https://image.tmdb.org/t/p/w780/%s";

    // @SerializedName tells Gson matching json/pojo properties
    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("original_title")
    String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    public Movie() {}

    public Movie(JSONObject jsonObject) {
        this.posterPath = jsonObject.optString("poster_path");
        this.originalTitle = jsonObject.optString("original_title");
        this.overview = jsonObject.optString("overview");
        this.voteAverage = jsonObject.optDouble("vote_average");
        this.releaseDate = jsonObject.optString("release_date");
        this.backdropPath = jsonObject.optString("backdrop_path");
    }

    /**
     * Returns a formatted URL for the poster image
     * @return Formatted Image URL
     */
    public String getPosterPath() {
        return String.format(POSTER_PATH, posterPath);
    }

    /**
     * Returns a formatted URL for the backdrop image
     * @return Formatted Image URL
     */
    public String getBackdropPath() {
        String path = backdropPath;
        if (backdropPath == null || backdropPath.isEmpty() || backdropPath == "null" ) {
            return getPosterPath();
        }
        return String.format(BACKDROP_PATH, path);
    }

    /**
     * Returns the movie title
     * @return String representing movie title
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * Returns the movie overview
     * @return String respresenting movie overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Returns the movie release date
     * @return String representing movie release date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Returns the movie vote average
     * @return Double representing movie vote average
     */
    public Double getVoteAverage() {
        return voteAverage;
    }

    public static class MovieResult {

        public static String DATE_FORMAT = "dd MMM yyy";

        private ArrayList<Movie> results;

        public MovieResult() {
            results = new ArrayList<>();
        }

        public ArrayList<Movie> getResults() {
            return results;
        }

        public static MovieResult parseJSON(String response) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat(DATE_FORMAT);
            Gson gson = gsonBuilder.create();
            MovieResult res = gson.fromJson(response, MovieResult.class);
            return res;
        }
    }
}
