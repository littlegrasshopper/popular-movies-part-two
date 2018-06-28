package com.example.android.popularmoviesstageone.model;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fonda on 6/25/18.
 */

public class MovieTrailer {
    private String mKey;
    private String mName;

    public MovieTrailer(JSONObject jsonObject) {
        this.mKey = jsonObject.optString("key");
        this.mName = jsonObject.optString("name");
    }

    public String getKey() {
        return mKey;
    }

    public String getTrailerName() {
        return mName;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public void setTrailerName(String name) {
        mName = name;
    }

    /**
     * Convenience class for returning list of movie trailers
     */
    public static class TrailerResult {

        private ArrayList<MovieTrailer> results;

        public TrailerResult() {
            results = new ArrayList<>();
        }

        public ArrayList<MovieTrailer> getResults() {
            return results;
        }
    }
}
