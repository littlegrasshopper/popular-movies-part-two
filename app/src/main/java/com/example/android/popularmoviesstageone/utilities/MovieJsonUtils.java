package com.example.android.popularmoviesstageone.utilities;

import com.example.android.popularmoviesstageone.model.Movie;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Utility functions to handle Movie JSON data.
 */
public final class MovieJsonUtils {
    /**
     * Creates a list of Movie objects
     * @param array Array of response objects
     * @return ArrayList of Movie objects
     */
    public static ArrayList<Movie> getMovieDataFromJson(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            results.add(new Movie(array.optJSONObject(i)));
        }

        return results;
    }
}
