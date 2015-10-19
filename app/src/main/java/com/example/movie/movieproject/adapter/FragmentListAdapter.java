package com.example.movie.movieproject.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.movie.movieproject.ImageClickListener;
import com.example.movie.movieproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rushabh on 10/10/15.
 */
public class FragmentListAdapter extends ArrayAdapter<FragmentData> {

    private Activity context;
    private List<FragmentData>objects;

    public FragmentListAdapter(Activity context, int resource, List<FragmentData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_list_single, null, true);
        //TextView textView = (TextView)rootView.findViewById(R.id.fragment_text);
      //  textView.setText(objects.get(position).getMovieId());
        ImageView imageView = (ImageView)rootView.findViewById(R.id.icon_img);
        Picasso.with(context).load(objects.get(position).getImageURI()).into(imageView);

        imageView.setOnClickListener(new ImageClickListener(context,objects.get(position).getMovieId(),objects.get(position).getImageURI()));

        return rootView;
    }






//    @Override
//    public void remove(FragmentData object) {
//    //    Picasso.with(context).invalidate(object.getImageURI());
//        super.remove(object);
//    }
}
