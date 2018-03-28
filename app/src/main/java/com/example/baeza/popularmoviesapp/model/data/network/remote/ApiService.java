package com.example.baeza.popularmoviesapp.model.data.network.remote;

import com.example.baeza.popularmoviesapp.model.data.network.model.movieDetail.MovieDetailRequest;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieList.MovieRequest;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieReview.MovieReview;
import com.example.baeza.popularmoviesapp.model.data.network.model.movieTrailer.MovieTrailer;

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

    @GET("popular?api_key")
    Observable<MovieRequest> getMovieListPopularityPage(@Query("api_key") String api_key,
                                                        @Query("page") int page);

    @GET("top_rated?api_key")
    Observable<MovieRequest> getMovieListTopRatedPage(@Query("api_key") String api_key,
                                                        @Query("page") int page);

    @GET("{id}?api_key")
    Observable<MovieDetailRequest> getMovieDetail(@Path("id") int id,
                                                  @Query("api_key") String api_key);

    @GET("{id}/videos?api_key")
    Observable<MovieTrailer> getMovieTrailer(@Path("id") int id,
                                             @Query("api_key") String api_key);

    @GET("{id}/reviews?api_key")
    Observable<MovieReview> getMovieReview(@Path("id") int id,
                                           @Query("api_key") String api_key);
}
