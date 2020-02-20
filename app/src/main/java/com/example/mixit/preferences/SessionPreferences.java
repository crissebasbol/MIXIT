package com.example.mixit.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mixit.models.Item;
import com.example.mixit.models.User;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class SessionPreferences {

    private User currentUser;
    private SharedPreferences preferences;

    private Context context;

    private final String PREFS_NAME = "MIXIT_SESSION_PREFS";
    private final String PREF_USER = "PREF_USER";
    private final String PREF_FAVOURITES = "PREF_FAVOURITES";
    private SharedPreferences.Editor editor;
    private FireBaseAuth fireBaseAuth;

    private static SessionPreferences INSTANCE;
    private static boolean REREAD_PREFERENCES = false;

    public SessionPreferences(Context context, Activity activity, @Nullable FireBaseAuth fireBaseAuth){
        this.context = context;
        if (fireBaseAuth == null){
            this.fireBaseAuth = new FireBaseAuth(context, activity);
        }else{
            this.fireBaseAuth = fireBaseAuth;
        }
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        REREAD_PREFERENCES = false;
        editor = preferences.edit();
        Gson gson = new Gson();
        if(this.fireBaseAuth.checkSigned()){
            String userJson = preferences.getString(PREF_USER, null);
            if (!TextUtils.isEmpty(preferences.getString(PREF_USER, null))) {
                currentUser = gson.fromJson(userJson, User.class);
            }
        }
        else{
            endSession();
        }
    }

    public static SessionPreferences get(Context context, Activity activity, @Nullable FireBaseAuth fireBaseAuth) {
        if (INSTANCE == null || REREAD_PREFERENCES) {
            if (fireBaseAuth != null)
                INSTANCE = new SessionPreferences(context, activity, fireBaseAuth);
            else
                INSTANCE = new SessionPreferences(context, activity, null);
        }
        return INSTANCE;
    }

    public void endSession(){
        currentUser = null;
        editor.putString(PREF_USER, null);
        editor.apply();
    }

    public void saveSessionUser(User user){
        user.getFullName();
        user.getEmail();
        currentUser = user;
        Gson gson = new Gson();
        String userJson =gson.toJson(user);
        editor.putString(PREF_USER, userJson);
        editor.apply();
        REREAD_PREFERENCES = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveFavourite(Item item){
        // Storing
        List<Item> favourites = getFavourites();
        if (favourites != null) {
            for (int i = 0; i < favourites.size(); i++){
                System.out.println(favourites.get(i).getTitle());
                if (favourites.get(i).getId() == item.getId()) {
                    // TODO: Handler for duplicate element.
                    break;
                }
            }
            favourites.add(item);
        } else {
            favourites = new ArrayList<>();
            favourites.add(item);
        }
        setFavourites(favourites);
//        REREAD_PREFERENCES = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFavourites (List<Item> favourites) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(favourites);
            so.flush();
            editor.putString(PREF_FAVOURITES, Base64.getEncoder().encodeToString(bo.toByteArray()));
            editor.apply();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Item> getFavourites () {
        List<Item> obj = null;
        try {
            String strFavourites = preferences.getString(PREF_FAVOURITES, null);
            byte b[] = Base64.getDecoder().decode(strFavourites);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj = (List<Item>) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

}
