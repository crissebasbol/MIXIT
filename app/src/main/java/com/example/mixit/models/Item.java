package com.example.mixit.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Item {
    private String title;
    private String description;
    private HashMap tutorial;
    private String id;
    private String image;
    private String creatorsEmail;
    private Date alarm;
    private Boolean favourite;
    private Boolean prepared;
    private JSONObject object;

    public Item() {	}

    public Item(JSONObject object) {
        this.object = object;
        HashMap tutorial = new HashMap();

        try {
            this.id = (String) object.get("idDrink");
            this.title = (String) object.get("strDrink");
            this.image = (String) object.get("strThumb");
            tutorial.put("instructions", object.get("strInstructions"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tutorial.put("ingredients", parseIngredients());

        this.tutorial = tutorial;

        this.description = parseDescription();

        this.creatorsEmail = creatorsEmail;

        this.alarm = alarm;
        this.favourite = favourite;
        this.prepared = prepared;
    }

    private String parseDescription() {
        String description = new String();
        try {
            if (!this.object.get("strCategory").toString().equals("null")) description += this.object.get("strCategory").toString() + " - ";
            if (!this.object.get("strIBA").toString().equals("null")) description += this.object.get("strIBA").toString() + " - ";
            if (!this.object.get("strAlcoholic").toString().equals("null")) description += this.object.get("strAlcoholic").toString() + " - ";
            if (!this.object.get("strGlass").toString().equals("null")) description += this.object.get("strGlass").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return description.trim();
    }

    private List<String> parseIngredients() {
        List<String> tutorial = new ArrayList<>();
        for (Integer i = 1; i <= 15; i++) {
            try {
                if (!(this.object.get("strIngredient"+i).toString().equals("null"))) {
                    if (!(this.object.get("strMeasure"+i).toString().equals("null"))) {
                        tutorial.add(this.object.get("strIngredient"+i).toString()+" - "+this.object.get("strMeasure"+i).toString().trim());
                    } else {
                        tutorial.add(this.object.get("strIngredient"+i).toString().trim());
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
