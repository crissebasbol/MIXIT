/*
 * Copyright (c)
 * Created by Cristhian Sebastian Bolaños Portilla (crissebas@unicauca.edu.co, crissebasbol@gmail.com)
 * All rights reserved
 */

package com.example.mixit.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mixit.R;
import com.example.mixit.activities.ShowItemActivity;
import com.example.mixit.models.Item;

import java.util.HashMap;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Item> {
    private Context mContext;

    public ListViewAdapter(Context context, int resource, List<Item> objects){
        super(context, resource, objects);

        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        final Item item = getItem(position);
        ImageView img = v.findViewById(R.id.product_photo);
        TextView txtTitle = (TextView) v.findViewById(R.id.product_title);
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
        txtTutorial.setText((CharSequence) tutorial.get("ingredients"));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, ShowItemActivity.class);
                intent.putExtra("item", item);
                mContext.startActivity(intent);
            }
        });

        return v;
    }
}
