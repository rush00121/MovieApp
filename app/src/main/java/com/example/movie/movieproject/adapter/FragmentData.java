package com.example.movie.movieproject.adapter;

/**
 * Created by rushabh on 10/10/15.
 */
public class FragmentData {

    private String imageURI;
    private String data;

    public FragmentData(String imageURI, String data) {
        this.imageURI = imageURI;
        this.data = data;
    }

    public String getImageURI() {
        return imageURI;
    }
    public String getData() {
        return data;
    }
}
