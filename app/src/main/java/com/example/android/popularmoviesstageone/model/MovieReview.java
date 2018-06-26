package com.example.android.popularmoviesstageone.model;

import org.json.JSONObject;

/**
 * Created by fonda on 6/25/18.
 */

public class MovieReview {

    private String author;
    private String content;

    public MovieReview(JSONObject jsonObject) {
        this.author = jsonObject.optString("author");
        this.content = jsonObject.optString("content");
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getContent() {
        return this.content;
    }
}
