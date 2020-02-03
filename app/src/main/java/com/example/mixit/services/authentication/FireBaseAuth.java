package com.example.mixit.services.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mixit.R;
import com.example.mixit.activities.GenericAbstractActivity;
import com.example.mixit.activities.MainActivity;
import com.example.mixit.activities.StartActivity;
import com.example.mixit.fragments.ProfileFragment;
import com.example.mixit.models.User;
import com.example.mixit.preferences.SessionPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FireBaseAuth {

    private static final String TAG = "FireBaseAuth";

    private FirebaseAuth mAuth;
    private Context context;
    private Activity activity;
    private FirebaseUser currentUser;
    private Toast toast;
    private SessionPreferences sessionPreferences;

    private boolean successfulPassword, successfulPhoto, successfulName, successfullDeleted;

    public FireBaseAuth(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        sessionPreferences = SessionPreferences.get(context, activity, this);
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
                }else{
                    showMessageUpdate();
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
                }else{
                    showMessageUpdate();
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
                successfulPassword = true;
                showMessageUpdate();
                try {
                    EditText password1 = activity.findViewById(R.id.password1);
                    EditText password2 = activity.findViewById(R.id.password2);
                    password1.setText("");
                    password2.setText("");
                }catch (Exception e){
                    Log.e(TAG, "exception", e);
                }
            }
        });
        return successfulPassword;
    }

    private void showMessageUpdate(){
        User user = new User(sessionPreferences.getCurrentUser().getEmail(), sessionPreferences.getCurrentUser().getFullName(),
                sessionPreferences.getCurrentUser().getPhoto());
            user.setFullName(currentUser.getDisplayName());
            if (currentUser.getPhotoUrl() != null) {
                user.setPhoto(currentUser.getPhotoUrl().toString());
            }
        if (!toast.getView().isShown()) {
            toast.setText(activity.getString(R.string.message_updated_successful));
            toast.show();
        }
        sessionPreferences.saveSessionUser(user);
        /*
        Intent intent = new Intent(activity, MainActivity.class);
        context.startActivity(intent);
         */
        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        TextView mTxtNameNavHeader =  header.findViewById(R.id.name_user);
        mTxtNameNavHeader.setText(this.sessionPreferences.getCurrentUser().getFullName());
        CircleImageView imageViewProfile = header.findViewById(R.id.profile_image);
        if (!sessionPreferences.getCurrentUser().getPhoto().equals("null")) {
            Picasso.get().load(sessionPreferences.getCurrentUser().getPhoto())
                    .resize(110, 110)
                    .centerCrop()
                    .into(imageViewProfile);
        }
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

    public void updateProfile(String name, String photo, @Nullable final String password){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(photo))
                .build();
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (password != null){
                            updatePassword(password);
                        }else{
                            showMessageUpdate();
                        }
                    }
                });
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }
}
