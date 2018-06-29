package com.example.android.popularmoviesstageone;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmoviesstageone.model.Movie;
import com.example.android.popularmoviesstageone.model.MovieReview;
import com.example.android.popularmoviesstageone.model.MovieTrailer;
import com.example.android.popularmoviesstageone.service.MoviesApiService;
import com.example.android.popularmoviesstageone.utilities.NetworkUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by fonda on 6/8/18.
 */

/**
 * MovieClient is a singleton that uses Retrofit to interact with the API service.
 */
public class MovieClient {

    private static MovieClient instance;
    private MoviesApiService service;

    private MovieClient() {
        /*
        HttpRequestInterceptor requestInterceptor = new HttpRequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("api_key", NetworkUtils.API_KEY);
            }

            @Override
            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {

            }
        };

        // Create a Retrofit instance with a Gson converter and
        // associate the adapter to the RequestInterceptor

        // Add the interceptor ot OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient().Builder();
        builder.interceptors().add(requestInterceptor);
        client.interceptors().add(requestInterceptor);

        */
        //final Gson gson =
        //        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                Log.d("MovieClient", "OriginalURL: " + originalHttpUrl);
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", NetworkUtils.API_KEY)
                        .build();
                /*
                Request request = original.newBuilder()
                        .header("api_key", NetworkUtils.API_KEY)
                        .method(original.method(), original.body())
                        .build();
                        */
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtils.API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();

        service = retrofit.create(MoviesApiService.class);
    }

    public static MovieClient getInstance() {
        if (instance == null) {
            instance = new MovieClient();
        }
        return instance;
    }

    /**
     * Invokes the network call via the MoviesApiService
     * @param category
     * @return
     */
    public Observable<Movie.MovieResult> getMovies(@NonNull String category) {
        return service.getMovies(category);
    }

    public Observable<MovieReview.ReviewResult> getReviews(@NonNull String movieId) {
        return service.getReviews(movieId);
    }

    public Observable<MovieTrailer.TrailerResult> getTrailers(@NonNull String movieId) {
        return service.getTrailers(movieId);
    }
}
