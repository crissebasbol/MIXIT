/*
 * Copyright (c)
 * Created by Cristhian Sebastian Bolaños Portilla (crissebas@unicauca.edu.co, crissebasbol@gmail.com)
 * All rights reserved
 */

package com.example.mixit.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mixit.R;
import com.example.mixit.models.Item;

import java.util.HashMap;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Item> {
    public ListViewAdapter(Context context, int resource, List<Item> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        Item item = getItem(position);
        ImageView img = v.findViewById(R.id.product_photo);
        TextView txtTitle = (TextView) v.findViewById(R.id.product_tittle);
        TextView txtDescription = (TextView) v.findViewById(R.id.product_description);
        TextView txtTutorial = (TextView) v.findViewById(R.id.product_tutorial);

        if(item.getImage() == null || item.getImage().equals("") || item.getImage().equals("null"))
            img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.no_image));
        else {
            //img.setImageBitmap(Utilities.convertStringToImag(item.getImage()));
        }
        txtTitle.setText(item.getTitle());
        txtDescription.setText(item.getDescription());
        HashMap tutorial = item.getTutorial();
        txtTutorial.setText((CharSequence) tutorial.get("instructions"));

        return v;
    }
}
