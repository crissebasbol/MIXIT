package com.example.mixit.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.Nullable;

import com.example.mixit.models.Item;
import com.example.mixit.models.User;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

// import java.util.Base64;

public class SessionPreferences {

    private User currentUser;
    private SharedPreferences preferences;

    private Context context;

    private static final String PREFS_NAME = "MIXIT_SESSION_PREFS";
    private static final String PREF_USER = "PREF_USER";
    public static final String PREF_FAVOURITES = "PREF_FAVOURITES";
    public static final String PREF_REMINDERS = "PREF_REMINDERS";
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

    public boolean deleteResource (String pref, Item item) {
        boolean deleted = false;
        int index = findResource(pref, item);
        if (index >= -1) {
            List<Item> resources = getPreferencesList(pref);
            resources.remove(index);
            setPreferencesList(resources, pref);
            deleted = true;
        }
        return deleted;
    }

    public boolean updateResource (String pref, Item item) {
        boolean updated = false;
        int index = findResource(pref, item);
        if (index >= -1) {
            List<Item> resources = getPreferencesList(pref);
            resources.set(index, item);
            setPreferencesList(resources, pref);
            updated = true;
        }
        return updated;
    }

    public Item readResource (String pref, Item item) {
        Item resource = null;
        int index = findResource(pref, item);
        if (index >= 0) {
            List<Item> resources = getPreferencesList(pref);
            resource = resources.get(index);
        }
        return resource;
    }

    public boolean createResource (String pref, Item item) {
        boolean created = false;
        int index = findResource(pref, item);
        if (index == -1) {
            List<Item> resources = getPreferencesList(pref);
            resources.add(item);
            setPreferencesList(resources, pref);
            created = true;
        }
        return created;
    }

    public boolean[] verifyResources (Item item) {
        boolean favouritePresent = findResource(PREF_FAVOURITES, item) >= 0;
        boolean reminderPresent = findResource(PREF_REMINDERS, item) >= 0;
        return new boolean[]{favouritePresent, reminderPresent};
    }

    public int findResource (String pref, Item item) {
        List<Item> resources = getPreferencesList(pref);
        int index = 0;
        boolean found = false;
        for (Item currentItem : resources) {
            if (item.getId().equals(currentItem.getId())) {
                found = true;
                break;
            }
            index++;
        }
        if (!found) { index = -1; }
        return index;
    }

    public void setPreferencesList (List<Item> preferenceList, String pref) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(preferenceList);
            so.flush();
            editor.putString(pref, Base64.encodeToString(bo.toByteArray(), Base64.DEFAULT));
            editor.apply();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Item> getPreferencesList (String pref) {
        List<Item> obj = null;
        try {
            String strFavourites = preferences.getString(pref, null);
            byte b[] = Base64.decode(strFavourites, Base64.DEFAULT);
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
