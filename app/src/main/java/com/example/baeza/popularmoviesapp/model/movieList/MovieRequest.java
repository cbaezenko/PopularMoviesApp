
package com.example.baeza.popularmoviesapp.model.movieList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieRequest implements Parcelable {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;


    public Integer getPage() {return page;}
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
    public Integer getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    public List<Result> getResults() {
        return results;
    }
    public void setResults(List<Result> results) {
        this.results = results;
    }


    //Parcelable settings
    public static final Creator<MovieRequest> CREATOR = new Creator<MovieRequest>() {
        @Override
        public MovieRequest createFromParcel(Parcel in) {
            return new MovieRequest(in);
        }
        @Override
        public MovieRequest[] newArray(int size) {
            return new MovieRequest[size];
        }
    };

    protected MovieRequest(Parcel in) {
        if (in.readByte() == 0) {
            page = null;
        } else {
            page = in.readInt();
        }
        if (in.readByte() == 0) {
            totalResults = null;
        } else {
            totalResults = in.readInt();
        }
        if (in.readByte() == 0) {
            totalPages = null;
        } else {
            totalPages = in.readInt();
        }
    }

    @Override public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(results.get(i).getPosterPath());
        parcel.writeInt(results.get(i).getId());
    }
}
