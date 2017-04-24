package com.example.sherlock.drumsticks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sherlock on 24/4/17.
 */

public class customAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] web;
    private final Bitmap imageId;
    public customAdapter(Activity context,
                         String[] web, Bitmap imageId) {
        super(context, R.layout.available_device_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate (R.layout.available_device_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);
        imageView.setImageBitmap(imageId);
        return rowView;
    }



}
