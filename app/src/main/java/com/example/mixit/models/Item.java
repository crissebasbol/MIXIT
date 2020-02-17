package com.example.mixit.models;

import android.graphics.Bitmap;

import com.example.mixit.interfaces.FetchCallback;
import com.example.mixit.interfaces.UpdateCallback;
import com.example.mixit.services.network.AssetFetch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Item implements Serializable, FetchCallback {
    private String title;
    private String description;
    private HashMap tutorial;
    private String id;
    private transient Bitmap image;
    private String imageUrl;
    private String creatorsEmail;
    private Date alarm;
    private Boolean favourite;
    private Boolean prepared;
    private transient UpdateCallback updateCallback;

    public Item(JSONObject object) {
        HashMap tutorial = new HashMap();
        HashMap params = new HashMap();
        AssetFetch assetFetch = new AssetFetch(this);
        params.put("type", "image");

        try {
            this.id = (String) object.get("idDrink");
            this.title = (String) object.get("strDrink");
            this.imageUrl = (String) object.get("strDrinkThumb");
            params.put("url", object.get("strDrinkThumb"));
            assetFetch.execute(params);
            tutorial.put("instructions", object.get("strInstructions"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tutorial.put("ingredients", parseIngredients(object));

        this.tutorial = tutorial;

        this.description = parseDescription(object);

        this.creatorsEmail = creatorsEmail;

        this.alarm = alarm;
        this.favourite = favourite;
        this.prepared = prepared;
    }

    /*
    public Item(String title, String imageUrl, String description, String creatorsEmail) {
        HashMap tutorial = new HashMap();
        this.title = title;
        this.imageUrl = imageUrl;
        tutorial.put("instructions", description);
            params.put("url", object.get("strDrinkThumb"));
            assetFetch.execute(params);
            tutorial.put("instructions", object.get("strInstructions"));


        tutorial.put("ingredients", parseIngredients(object));

        this.tutorial = tutorial;

        this.description = parseDescription(object);

        this.creatorsEmail = creatorsEmail;

        this.alarm = alarm;
        this.favourite = favourite;
        this.prepared = prepared;
    }

     */

    private String parseDescription(JSONObject object) {
        String description = new String();
        try {
            if (!object.get("strCategory").toString().equals("null")) description += object.get("strCategory").toString() + "\n";
            if (!object.get("strIBA").toString().equals("null")) description += object.get("strIBA").toString() + "\n";
            if (!object.get("strAlcoholic").toString().equals("null")) description += object.get("strAlcoholic").toString() + "\n";
            if (!object.get("strGlass").toString().equals("null")) description += object.get("strGlass").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(description.trim());
        return description.trim();
    }

    private String parseIngredients(JSONObject object) {
        String tutorial = "";
        for (Integer i = 1; i <= 15; i++) {
            try {
                if (!(object.get("strIngredient"+i).toString().equals("null"))) {
                    if (!(object.get("strMeasure"+i).toString().equals("null"))) {
                        tutorial += "•\t" + object.get("strIngredient"+i).toString()
                                +" - "+object.get("strMeasure"+i).toString().trim() + "\n";
                    } else {
                        tutorial += "•\t" + object.get("strIngredient"+i).toString().trim() + "\n";
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tutorial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap getTutorial() {
        return tutorial;
    }

    public void setTutorial(HashMap tutorial) {
        this.tutorial = tutorial;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getCreatorsEmail() {
        return creatorsEmail;
    }

    public void setCreatorsEmail(String creatorsEmail) {
        this.creatorsEmail = creatorsEmail;
    }

    public Date getAlarm() {
        return alarm;
    }

    public void setAlarm(Date alarm) {
        this.alarm = alarm;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Boolean getPrepared() {
        return prepared;
    }

    public void setPrepared(Boolean prepared) {
        this.prepared = prepared;
    }

    public void setUpdateCallback(UpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
    }

    @Override
    public void onSuccess(Bitmap picture) {
        this.image = picture;
        if (this.updateCallback != null) this.updateCallback.onUpdate(this.id);
    }
}
