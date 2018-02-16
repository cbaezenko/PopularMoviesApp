package com.example.baeza.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.baeza.popularmoviesapp.utilities.JsonUtilities;
import com.example.baeza.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONException;

/**
 * Created by baeza on 16.02.2018.
 */

public class MovieDetail extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);
    }

    private void askForTrailers() throws JSONException {
        JsonUtilities.parseMovieTrailerJSON("string json");
        //
    }
}
