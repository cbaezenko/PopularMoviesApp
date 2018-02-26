package com.example.baeza.popularmoviesapp.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by baeza on 26.02.2018.
 */

public interface ApiService {
    @GET("top_rated?api_key={api_key}")
    Call getMovieListRated(@Path("api_key") String api_key);

    @GET("popularity?api_key={api_key}")
    Call getMovieListPopularity(@Path("api_key") String api_key);
}
