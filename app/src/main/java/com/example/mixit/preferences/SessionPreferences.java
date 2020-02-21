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
import java.util.Base64;
import java.util.List;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveFavourite(Item item){
        List<Item> favourites = getPreferenceList(PREF_FAVOURITES);
        if (favourites != null) {
            if (!verifyFavourites(favourites, item)) favourites.add(item);
        } else {
            favourites = new ArrayList<>();
            favourites.add(item);
        }
        setPreferenceList(favourites, PREF_FAVOURITES);
//        REREAD_PREFERENCES = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveReminder(Item item){
        List<Item> reminders = getPreferenceList(PREF_REMINDERS);
        if (reminders != null) {
            if (!verifyReminders(reminders, item)) reminders.add(item);
        } else {
            reminders = new ArrayList<>();
            reminders.add(item);
        }
        setPreferenceList(reminders, PREF_REMINDERS);
//        REREAD_PREFERENCES = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean verifyFavourites (@Nullable List<Item> favourites, Item item) {
        boolean present = false;
        boolean listNull = true;

        if (favourites == null) {
            favourites = getPreferenceList(PREF_FAVOURITES);
        } else {
            listNull = false;
        }

        if (favourites != null) {
            for (int i = 0; i < favourites.size(); i++){
                if (favourites.get(i).getId().equals(item.getId())) {
                    if (listNull) {
                        favourites.remove(i);
                    }
                    present = true;
                    break;
                }
            }
            setPreferenceList(favourites, PREF_FAVOURITES);
        }
        return present;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean verifyReminders (@Nullable List<Item> reminders, Item item) {
        boolean present = false;
        boolean isNull = true;

        if (reminders == null) {
            reminders = getPreferenceList(PREF_REMINDERS);
            isNull = reminders != null ? false : true;
        }

        if (!isNull) {
            for (int i = 0; i < reminders.size(); i++){
                if (reminders.get(i).getId().equals(item.getId())) {
                    if (isNull) {
                        reminders.remove(i);
                        setPreferenceList(reminders, PREF_REMINDERS);
                    }
                    present = true;
                    break;
                }
            }
        }
        return present;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPreferenceList (List<Item> preferenceList, String pref) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(preferenceList);
            so.flush();
            editor.putString(pref, Base64.getEncoder().encodeToString(bo.toByteArray()));
            editor.apply();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Item> getPreferenceList (String pref) {
        List<Item> obj = null;
        try {
            String strFavourites = preferences.getString(pref, null);
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
