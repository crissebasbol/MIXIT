package com.example.mixit.models;

import java.util.Date;

public class Item {
    private String title;
    private String description;
    private String tutorial;
    private int id;
    private String image;
    private String creatorsEmail;
    private Date alarm;
    private Boolean favourite;
    private Boolean prepared;


    public Item() {	}

    public Item(String title, String description, String tutorial, int id, String image, String creatorsEmail, Date alarm, Boolean favourite, Boolean prepared) {
        this.title = title;
        this.description = description;
        this.tutorial = tutorial;
        this.id = id;
        this.image = image;
        this.creatorsEmail = creatorsEmail;
        this.alarm = alarm;
        this.favourite = favourite;
        this.prepared = prepared;
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

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
