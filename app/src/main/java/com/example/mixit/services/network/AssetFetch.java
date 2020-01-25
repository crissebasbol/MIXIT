package com.example.mixit.services.network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.example.mixit.R;
import com.example.mixit.interfaces.FetchCallback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class AssetFetch extends AsyncTask<HashMap, Void, Object> {
    private FetchCallback mCallback;

    public AssetFetch (FetchCallback callback) {
        this.mCallback = callback;
    }

    public void fetchImage (String imageUrl) {
        URL url = null;
        Bitmap picture = null;
        try {
            url = new URL(imageUrl);
            picture = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            mCallback.onSuccess(picture);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(HashMap... params) {
        switch (params[0].get("type").toString()) {
            case "image":
                fetchImage(params[0].get("url").toString());
                break;
        }
        return null;
    }
}
