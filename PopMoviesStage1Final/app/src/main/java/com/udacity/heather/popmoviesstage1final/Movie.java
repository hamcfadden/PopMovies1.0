package com.udacity.heather.popmoviesstage1final;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String mIdMoviePoster;
    private String mOriginalTitle;
    private Uri mPosterPathUri;
    private String mMovieOverview;
    private double mHighestRated;
    private int mAverageVote;
    private String mReleaseDate;

    //Constructor to create an instance of the Movie Item

    Movie(String id, String title, Uri posterPathUri, String overview, double rating, int voteCount, String releaseDate) {
        mIdMoviePoster = id;
        mOriginalTitle = title;
        mPosterPathUri = posterPathUri;
        mMovieOverview = overview;
        mHighestRated = rating;
        mAverageVote = voteCount;
        mReleaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        mIdMoviePoster = in.readString();
        mOriginalTitle = in.readString();
        mPosterPathUri = in.readParcelable(Uri.class.getClassLoader());
        mMovieOverview = in.readString();
        mHighestRated = in.readDouble();
        mAverageVote = in.readInt();
        mReleaseDate = in.readString();
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


    public String getId() {
        return mIdMoviePoster;
    }

    public String getTitle() {
        return mOriginalTitle;
    }

    public Uri getPosterPathUri() {
        return mPosterPathUri;
    }

    public String getOverview() { return mMovieOverview; }

    public Double getRating() {
        return mHighestRated;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mIdMoviePoster);
        parcel.writeString(mOriginalTitle);
        parcel.writeParcelable(mPosterPathUri, i);
        parcel.writeString(mMovieOverview);
        parcel.writeDouble(mHighestRated);
        parcel.writeInt(mAverageVote);
        parcel.writeString(mReleaseDate);

    }
}




