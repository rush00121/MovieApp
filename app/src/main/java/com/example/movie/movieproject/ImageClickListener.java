package com.example.movie.movieproject;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by rushabh on 10/18/15.
 */
public class ImageClickListener implements View.OnClickListener {

    Activity parentActivity;
    String movieId;
    String imageUri;

    public ImageClickListener(Activity activity, String movieId,String imageUri) {
        parentActivity = activity;
        this.movieId = movieId;
        this.imageUri = imageUri;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(parentActivity, DetailActivity.class);
        intent.putExtra("movieId",movieId);
        intent.putExtra("imageUri",imageUri);
        parentActivity.startActivity(intent);
    }
}
