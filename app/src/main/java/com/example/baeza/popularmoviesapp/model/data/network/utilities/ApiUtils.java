package com.example.baeza.popularmoviesapp.model.data.network.utilities;

import com.example.baeza.popularmoviesapp.model.data.network.remote.ApiService;
import com.example.baeza.popularmoviesapp.model.data.network.remote.RetrofitClient;


/**
 * Created by baeza on 26.02.2018.
 */

public class ApiUtils {

    private static final String BASE_URL_MOVIE = "https://api.themoviedb.org/3/movie/";
    private static final String IMAGE_MOVIE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String RECOMMENDED_SIZE_MOBILE = "w185/";
    private final static String URL_BASE_FOR_IMAGE_MOVIE = IMAGE_MOVIE_BASE_URL + RECOMMENDED_SIZE_MOBILE;

    private final static String BASE_YOUTUBE_VIDEOS = "http://www.youtube.com/watch?v=";

    public static ApiService getApiService(){
        return RetrofitClient.getClient(BASE_URL_MOVIE).create(ApiService.class);
    }

    public static String getUrlBaseForImageMovie() {return URL_BASE_FOR_IMAGE_MOVIE;}
    public static String getBaseYoutubeVideos(){return BASE_YOUTUBE_VIDEOS;}
}
