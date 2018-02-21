package com.example.baeza.popularmoviesapp.utilities;

import android.util.Log;

import com.example.baeza.popularmoviesapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by baeza on 16.02.2018.
 */

public class JsonUtilities {

    private static String TAG = JsonUtilities.class.getSimpleName();
   // public static List<Movie> sMovieListMainScreen = new ArrayList<>();

    public static Movie parseDetailMovieJSON(String json) throws JSONException{
        JSONObject movie = new JSONObject(json);
        String overview = movie.optString("overview");
        int runtime = movie.optInt("runtime");
        Double vote_average = movie.optDouble("vote_average");
        String title = movie.getString("title");
        String release_date = movie.getString("release_date");
        String poster_path = NetworkUtils.getUrlBaseForImageMovie() +
                movie.optString("poster_path");

        return new Movie(vote_average,
                title,
                release_date,
                poster_path,
                overview,
                runtime);
    }

    public static List<Movie> parseMoviesListJSON(String json) throws JSONException{
        JSONObject movies = new JSONObject(json);
        JSONArray results = movies.optJSONArray("results");

        List<Movie> movieList = new ArrayList<Movie>();
        for(int i=0; i<results.length();i++){
            JSONObject result_value_i = results.getJSONObject(i);
            int id_i = result_value_i.optInt("id");
            String poster_path_i = result_value_i.optString("poster_path");

            Movie movie = new Movie(poster_path_i, id_i);
            movieList.add(movie);
        }
        return movieList;
    }

    public static Movie parseMovieTrailerJSON(String json) throws JSONException{
        List<String> trailerList = new ArrayList<>();
        JSONObject movie = new JSONObject(json);
        JSONArray results = movie.getJSONArray("results");
         for(int i = 0; i<results.length();i++){
             trailerList.add(results.optString(i));
         }
        return  new Movie(trailerList);
    }
}
