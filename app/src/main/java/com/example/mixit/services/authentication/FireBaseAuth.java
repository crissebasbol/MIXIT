package com.example.mixit.services.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mixit.R;
import com.example.mixit.activities.MainActivity;
import com.example.mixit.activities.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FireBaseAuth {

    private static final String TAG = "FireBaseAuth";

    private FirebaseAuth mAuth;
    private Context context;
    private Activity activity;
    private FirebaseUser currentUser;

    private boolean successfulPassword, successfulPhoto, successfulName, successfullDeleted;

    public FireBaseAuth(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
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

    public boolean updateNameFireBase(String name, @Nullable final Class T){
        successfulName = false;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.v(TAG, "Name updated successful");
                successfulName = true;
                if (T != null){
                    Toast.makeText(context, R.string.txt_successful_register, Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, T));
                    ((Activity) context).finish();
                }
            }
        });
        return successfulName;
    }

    public boolean updatePhotoUri(String url, @Nullable final Class T){
        successfulPhoto = false;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(url))
                .build();
        currentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.v(TAG, "Profile photo updated successful");
                successfulPhoto = true;
                if (T != null){
                    context.startActivity(new Intent(context, T));
                    ((Activity) context).finish();
                }
            }
        });
        return successfulPhoto;
    }

    public boolean updatePassword(String password){
        successfulPassword = false;
        currentUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.v(TAG, "User password updated successful");
                successfulPassword = true;
            }
        });
        return successfulPassword;
    }

    public boolean deleteUser(){
        successfullDeleted = false;
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.v(TAG, "User account deleted");
                successfullDeleted = true;
            }
        });
        return successfullDeleted;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }
}
