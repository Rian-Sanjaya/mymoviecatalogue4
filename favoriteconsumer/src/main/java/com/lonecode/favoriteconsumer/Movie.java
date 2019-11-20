package com.lonecode.favoriteconsumer;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private int photo;
    private String movieid;
    private String name;
    private String description;
    private String userScore;
    private String releaseDate;
    private String originalLanguage;
    private String posterPath;
    private String category;

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public Movie() {
    }

    public Movie(String movieid, String name, String description, String userScore, String releaseDate, String originalLanguage, String posterPath, String category) {
        this.movieid = movieid;
        this.name = name;
        this.description = description;
        this.userScore = userScore;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.posterPath = posterPath;
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.photo);
        dest.writeString(this.movieid);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.userScore);
        dest.writeString(this.releaseDate);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.posterPath);
        dest.writeString(this.category);
    }

    protected Movie(Parcel in) {
        this.photo = in.readInt();
        this.movieid = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.userScore = in.readString();
        this.releaseDate = in.readString();
        this.originalLanguage = in.readString();
        this.posterPath = in.readString();
        this.category = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
