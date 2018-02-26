package com.example.baeza.popularmoviesapp.utilities;

import com.example.baeza.popularmoviesapp.remote.ApiService;
import com.example.baeza.popularmoviesapp.remote.RetrofitClient;

/**
 * Created by baeza on 26.02.2018.
 */

public class ApiUtils {
    private static final String BASE_URL_MOVIE =
            "https://api.themoviedb.org/3/movie/";

    private static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";

    private static ApiService getApiServiceMovieList(){
        return RetrofitClient.getClient(BASE_URL_MOVIE).create(ApiService.class);
    }

//    private static ApiService getApiServiceImage(){
//        return RetrofitClient.getClient(BASE_URL_IMAGE).create(ApiService.class);
//    }
}
