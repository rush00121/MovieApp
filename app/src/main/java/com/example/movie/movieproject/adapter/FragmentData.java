package com.example.movie.movieproject.adapter;

/**
 * Created by rushabh on 10/10/15.
 */
public class FragmentData {

    private String imageURI;
    private String movieId;

    public FragmentData(String imageURI, String movieId) {
        this.imageURI = imageURI;
        this.movieId = movieId;
    }

    public String getImageURI() {
        return imageURI;
    }
    public String getMovieId() {
        return movieId;
    }
}
