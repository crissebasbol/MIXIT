package com.example.mixit.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Item implements Serializable {
    private String title;
    private String description;
    private HashMap tutorial;
    private String id;
    private String image;
    private String creatorsEmail;
    private Date alarm;
    private Boolean favourite;
    private Boolean prepared;

    public Item(JSONObject object) {
        HashMap tutorial = new HashMap();

        try {
            this.id = (String) object.get("idDrink");
            this.title = (String) object.get("strDrink");
            this.image = (String) object.get("strDrinkThumb");
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

    private String parseDescription(JSONObject object) {
        String description = new String();
        try {
            if (!object.get("strCategory").toString().equals("null")) description += object.get("strCategory").toString() + " - ";
            if (!object.get("strIBA").toString().equals("null")) description += object.get("strIBA").toString() + " - ";
            if (!object.get("strAlcoholic").toString().equals("null")) description += object.get("strAlcoholic").toString() + " - ";
            if (!object.get("strGlass").toString().equals("null")) description += object.get("strGlass").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return description.trim();
    }

    private List<String> parseIngredients(JSONObject object) {
        List<String> tutorial = new ArrayList<>();
        for (Integer i = 1; i <= 15; i++) {
            try {
                if (!(object.get("strIngredient"+i).toString().equals("null"))) {
                    if (!(object.get("strMeasure"+i).toString().equals("null"))) {
                        tutorial.add(object.get("strIngredient"+i).toString()+" - "+object.get("strMeasure"+i).toString().trim());
                    } else {
                        tutorial.add(object.get("strIngredient"+i).toString().trim());
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

}
