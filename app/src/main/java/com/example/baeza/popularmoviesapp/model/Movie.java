package com.example.baeza.popularmoviesapp.model;

import java.util.Date;
import java.util.List;

/**
 * Created by baeza on 16.02.2018.
 */

public class Movie {
    private String title, overview, poster_path;
    private Date release_date;
    int runtime, id;
    double vote_average;

    //constructor for trailer
    public Movie(List<String> trailerList){}

    //constructor for list in the MainScreen
    public Movie(String poster_path, int id){
        this.id = id;
        this.poster_path = poster_path;
    }

    //constructor for detail movie activity
    public Movie(double vote_average,
                 String title,
                 String poster_path,
                 String overview,
                 Date release_date,
                 int runtime){
        this.poster_path = poster_path;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.runtime = runtime;
    }

    public Date getRelease_date() {return release_date;}
    public String getTitle() {return title;}
    public String getOverview() {return overview;}
    public int getRuntime() {return runtime;}
    public int getId() {return id;}
    public double getVote_average() {return vote_average;}
}
