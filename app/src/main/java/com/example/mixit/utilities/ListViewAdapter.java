/*
 * Copyright (c)
 * Created by Cristhian Sebastian Bolaños Portilla (crissebas@unicauca.edu.co, crissebasbol@gmail.com)
 * All rights reserved
 */

package com.example.mixit.utilities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mixit.R;
import com.example.mixit.fragments.main.ShowFragment;
import com.example.mixit.interfaces.UpdateCallback;
import com.example.mixit.models.Item;
import com.example.mixit.services.assets.BlurImages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Item> implements UpdateCallback {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private List<Item> mItems = new ArrayList<>();
    private ViewGroup mViewGroup;

    public ListViewAdapter(FragmentManager fragmentManager, Context context, int resource, List<Item> objects){
        super(context, resource, objects);

        this.mContext = context;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("Executing shit");
        View v = convertView;
        mViewGroup = parent;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        final Item item = getItem(position);
        ImageView img = v.findViewById(R.id.product_photo);
        TextView txtTitle = v.findViewById(R.id.product_title);
        TextView txtDescription = v.findViewById(R.id.product_description);
        TextView txtTutorial = v.findViewById(R.id.product_tutorial);

        if(item.getImage() != null) {
            v.findViewById(R.id.loading_panel).setVisibility(View.GONE);
            img.setImageBitmap(item.getImage());
        } else {
            mItems.add(item);
            v.setTag(item.getId());
            item.setUpdateCallback(this);
        }

        txtTitle.setText(item.getTitle());
        txtDescription.setText(item.getDescription());
        HashMap tutorial = item.getTutorial();
        txtTutorial.setText((CharSequence) tutorial.get("ingredients"));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFragment showFragment = new ShowFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                showFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, showFragment);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

    @Override
    public void onUpdate(final String itemId) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View view = mViewGroup.findViewWithTag(itemId);
                if (view != null) {
                    for (Item item : mItems) {
                        if (item.getId().equals(itemId)) {
                            view.findViewById(R.id.loading_panel).setVisibility(View.GONE);
                            ((ImageView) view.findViewById(R.id.product_photo))
                                    .setImageBitmap(item.getImage());
                            break;
                        }
                    }
                }
            }
        });
    }
}


