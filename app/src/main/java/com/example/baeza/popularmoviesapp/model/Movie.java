package com.example.baeza.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by baeza on 16.02.2018.
 */

public class Movie implements Parcelable{
    private  String title, overview, poster_path,release_date;
    private  int runtime, id;
    private  double vote_average;

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
                 String release_date,
                 String poster_path,
                 String overview,
                 int runtime){
        this.poster_path = poster_path;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.runtime = runtime;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        runtime = in.readInt();
        id = in.readInt();
        vote_average = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getRelease_date(){return release_date;}
    public String getTitle() {return title;}
    public String getOverview() {return overview;}
    public String getPoster_path() {return poster_path;}
    public int getRuntime() {return runtime;}
    public int getId() {return id;}
    public double getVote_average() {return vote_average;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(poster_path);
        out.writeInt(id);
    }
}
