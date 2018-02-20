package com.example.baeza.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.baeza.popularmoviesapp.utilities.JsonUtilities;
import com.example.baeza.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONException;

/**
 * Created by baeza on 16.02.2018.
 */

public class MovieDetail extends AppCompatActivity {

    public final static String TITLE_KEY = "title_key";
    public final static String POSTER_PATH = "poster_path_key";
    public final static String OVERVIEW = "overview_key";
    public final static String RUNTIME = "runtime_key";
    public final static String VOTE_AVERAGE = "vote_average";
    TextView tv_title;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);
        tv_title = findViewById(R.id.tv_title);

        tv_title.setText(getIntent().getStringExtra(TITLE_KEY));
    }

    private void askForTrailers() throws JSONException {
        JsonUtilities.parseMovieTrailerJSON("string json");
    }
}
