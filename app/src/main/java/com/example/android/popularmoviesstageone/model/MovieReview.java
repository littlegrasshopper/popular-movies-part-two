package com.example.android.popularmoviesstageone.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fonda on 6/25/18.
 */

/**
 * Model for storing movie reviews.
 */
public class MovieReview {

    private String author;
    private String content;
    @SerializedName("url")
    private String reviewUrl;

    public MovieReview(JSONObject jsonObject) {
        this.author = jsonObject.optString("author");
        this.content = jsonObject.optString("content");
        this.reviewUrl = jsonObject.optString("url");
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReviewUrl(String url) {
        this.reviewUrl = url;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getContent() {
        return this.content;
    }

    public String getReviewUrl() {
        return this.reviewUrl;
    }

    /**
     * Convenience class for returning list of movie reviews
     */
    public static class ReviewResult {

        private ArrayList<MovieReview> results;

        public ReviewResult() {
            results = new ArrayList<>();
        }

        public ArrayList<MovieReview> getResults() {
            return results;
        }
    }
}
