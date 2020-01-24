package com.example.mixit.services.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.mixit.activities.MainActivity;
import com.example.mixit.activities.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseAuth {
    private FirebaseAuth mAuth;
    private Context context;
    private Activity activity;
    private FirebaseUser currentUser;

    public FireBaseAuth(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean checkSigned(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            return false;
        }
        else{
            return true;
        }
    }

    private void setLogin() {
        Intent intent = new Intent(activity, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        activity.finish();
    }

    public void setMainActivity(){
        if (currentUser != null){
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }
}
