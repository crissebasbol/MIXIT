/*
 * Copyright (c)
 * Created by Cristhian Sebastian Bolaños Portilla (crissebas@unicauca.edu.co, crissebasbol@gmail.com)
 * All rights reserved
 */

package com.example.mixit.models;


import android.net.Uri;

public class User {

    private String email;
    private String fullName;
    private String photo;


    public User(String email, String fullName, String photo) {
        this.email = email;
        this.fullName = fullName;
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
