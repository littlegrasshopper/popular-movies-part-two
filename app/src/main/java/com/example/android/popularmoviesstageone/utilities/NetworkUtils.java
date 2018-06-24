package com.example.android.popularmoviesstageone.utilities;

import android.util.Log;

import com.example.android.popularmoviesstageone.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utilities to communicate with the Movies DB.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "?api_key=";
    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVORITES = "favorites";

    private static String[] filterTypes = new String[]{POPULAR, TOP_RATED, FAVORITES};

    /**
     * Return the movieDB url for the given filter position.
     * @param apiFilterPosition Integer representing the supported filter types.
     * @return URL string for the movieDB API.
     */
    public static String getMoviesUrl(int apiFilterPosition) {
        return API_BASE_URL + getFilterType(apiFilterPosition) + API_KEY_PARAM + getApiKey();
    }

    public static String getFilterType(int filterPosition) {
        String filter = POPULAR;
        if (filterPosition < filterTypes.length) {
            filter = filterTypes[filterPosition];
        }
        Log.d(TAG, "Filter=" + filter);
        return filter;
    }

    /**
     * Returns themovieDB API key.
     * @return String representing the API key.
     */
    private static String getApiKey() {
        return "";
    }

    /**
     * Builds the URL used to fetch using themovieDB API.
     *
     * @param filter The filter that will be used to determine sort order.
     * @return The URL to use to query the movieDB server.
     */
    public static URL buildUrl(int filter) {
        URL url = null;
        try {
            url = new URL(getMoviesUrl(filter));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
