package com.example.baeza.popularmoviesapp.remote;

import com.example.baeza.popularmoviesapp.model.movieDetail.MovieDetailRequest;
import com.example.baeza.popularmoviesapp.model.movieList.MovieRequest;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by baeza on 26.02.2018.
 */

public interface ApiService {
    @GET("top_rated?api_key")
    Observable<MovieRequest> getMovieListRated(@Query("api_key") String api_key);

    @GET("popular?api_key")
    Observable<MovieRequest> getMovieListPopularity(@Query("api_key") String api_key);

    @GET("{id}?api_key")
    Observable<MovieDetailRequest> getMovieDetail(@Path("id") int  id,
                                                    @Query("api_key") String api_key);
}
