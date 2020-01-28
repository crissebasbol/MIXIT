package com.example.mixit.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mixit.R;
import com.example.mixit.interfaces.FetchCallback;
import com.example.mixit.models.Item;
import com.example.mixit.services.assets.BlurImages;
import com.example.mixit.services.network.AssetFetch;

import java.util.HashMap;

public class ShowItemActivity extends AppCompatActivity implements FetchCallback {
    private Item item;
    private ImageView picture;
    private Context mContext = this;
    private TextView description, tutorial, ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        this.item = (Item) getIntent().getSerializableExtra("item");

        AssetFetch assetFetch = new AssetFetch(this);
        HashMap params = new HashMap();
        params.put("type", "image");
        params.put("url", item.getImageUrl());
        assetFetch.execute(params);

        DisplayMetrics display = getResources().getDisplayMetrics();
        int width = display.widthPixels;
        picture = findViewById(R.id.cocktail_picture);
        picture.getLayoutParams().height =(int) (width*0.8);
        description = findViewById(R.id.cocktail_description);
        tutorial = findViewById(R.id.cocktail_tutorial);
        ingredients = findViewById(R.id.cocktail_ingredients);
        description.setText(item.getDescription());
        tutorial.setText(item.getTutorial().get("instructions").toString());
        ingredients.setText(item.getTutorial().get("ingredients").toString());
        setTitle(item.getTitle());
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        this.finish();
        return true;
    }

    @Override
    public void onSuccess(final Bitmap fetchedPicture) {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                findViewById(R.id.loading_panel).setVisibility(View.GONE);
                final Bitmap frontPicture = fetchedPicture.copy(fetchedPicture.getConfig(), false);
                Drawable drawablePicture = new BitmapDrawable(getResources(), BlurImages.blur(mContext, fetchedPicture));
                picture.setBackground(drawablePicture);
                picture.setImageBitmap(frontPicture);
            }
        });
    }
}
