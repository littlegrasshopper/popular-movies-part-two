package com.example.android.popularmoviesstageone.model;

import org.json.JSONObject;

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
}
