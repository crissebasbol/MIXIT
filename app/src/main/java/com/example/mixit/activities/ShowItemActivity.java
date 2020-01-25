package com.example.mixit.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        params.put("url", item.getImage());
        assetFetch.execute(params);

        DisplayMetrics display = getResources().getDisplayMetrics();
        int width = display.widthPixels;
        picture = findViewById(R.id.cocktail_picture);
        picture.getLayoutParams().height = (int) (width/1.6);
    }

    @Override
    public void onSuccess(final Bitmap fetchedPicture) {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                final Bitmap frontPicture = fetchedPicture.copy(fetchedPicture.getConfig(), false);
                Drawable drawablePicture = new BitmapDrawable(getResources(), BlurImages.blur(mContext, fetchedPicture));
                picture.setBackground(drawablePicture);
                picture.setImageBitmap(frontPicture);
            }
        });
    }
}
