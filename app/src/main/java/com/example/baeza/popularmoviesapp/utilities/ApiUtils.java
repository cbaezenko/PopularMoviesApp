package com.example.baeza.popularmoviesapp.utilities;

import com.example.baeza.popularmoviesapp.remote.ApiService;
import com.example.baeza.popularmoviesapp.remote.RetrofitClient;

/**
 * Created by baeza on 26.02.2018.
 */

public class ApiUtils {

    private static final String BASE_URL_MOVIE = "https://api.themoviedb.org/3/movie/";
    private static final String IMAGE_MOVIE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String RECOMMENDED_SIZE_MOBILE = "w185/";
    private final static String URL_BASE_FOR_IMAGE_MOVIE = IMAGE_MOVIE_BASE_URL + RECOMMENDED_SIZE_MOBILE;

    public static ApiService getApiServiceMovieList(){
        return RetrofitClient.getClient(BASE_URL_MOVIE).create(ApiService.class);
    }

    public static ApiService getApiServiceMovieDetail(){
        return RetrofitClient.getClient(BASE_URL_MOVIE).create(ApiService.class);
    }

    public static String getUrlBaseForImageMovie() {return URL_BASE_FOR_IMAGE_MOVIE;}
}
