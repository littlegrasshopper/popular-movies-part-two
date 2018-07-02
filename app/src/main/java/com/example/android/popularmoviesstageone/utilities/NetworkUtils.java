package com.example.android.popularmoviesstageone.utilities;

import android.util.Log;

import com.example.android.popularmoviesstageone.BuildConfig;

/**
 * Utilities to communicate with the Movies DB.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVORITES = "favorites";

    private static String[] filterTypes = new String[]{POPULAR, TOP_RATED, FAVORITES};


    public static String getFilterType(int filterPosition) {
        String filter = POPULAR;
        if (filterPosition < filterTypes.length) {
            filter = filterTypes[filterPosition];
        }
        Log.d(TAG, "Filter=" + filter);
        return filter;
    }
}
