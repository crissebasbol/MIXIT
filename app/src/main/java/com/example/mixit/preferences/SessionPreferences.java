package com.example.mixit.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.mixit.models.User;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.google.gson.Gson;

public class SessionPreferences {

    private User currentUser;
    private SharedPreferences preferences;

    private Context context;

    private final String PREFS_NAME = "MIXIT_SESSION_PREFS";
    private final String PREF_USER = "PREF_USER";
    private SharedPreferences.Editor editor;
    private FireBaseAuth fireBaseAuth;

    private static SessionPreferences INSTANCE;
    private static boolean REREAD_PREFERENCES = false;

    public SessionPreferences(Context context, Activity activity){
        this.context = context;
        fireBaseAuth = new FireBaseAuth(context, activity);
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        REREAD_PREFERENCES = false;
        editor = preferences.edit();
        Gson gson = new Gson();
        if(fireBaseAuth.checkSigned()){
            String userJson = preferences.getString(PREF_USER, null);
            if (!TextUtils.isEmpty(preferences.getString(PREF_USER, null))) {
                currentUser = gson.fromJson(userJson, User.class);
            }
        }
        else{
            endSession();
        }
    }

    public static SessionPreferences get(Context context, Activity activity) {
        if (INSTANCE == null || REREAD_PREFERENCES) {
            INSTANCE = new SessionPreferences(context, activity);
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

    public User getCurrentUser() {
        return this.currentUser;
    }

}
