package com.example.mixit.services.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mixit.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AnonymousAuth {
    private static final String TAG = "AnonymousAuth";

    private FirebaseAuth mAuth;
    private Context mContext;

    public AnonymousAuth(Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = context;
        signInAnonymously();
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            ((Activity) mContext).finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
//                            Toast.makeText(StartActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

//    TODO: THIS IS NOT A CRITICAL FEATURE.
//    private void linkAccount(String email, String password) {
//        // Make sure form is valid
//        if (!validateLinkForm(email, password)) {
//            return;
//        }
//
//        // Create EmailAuthCredential with email and password
//        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
//
//        // [START link_credential]
//        mAuth.getCurrentUser().linkWithCredential(credential)
//                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "linkWithCredential:success");
//                            FirebaseUser user = task.getResult().getUser();
//                        } else {
//                            Log.w(TAG, "linkWithCredential:failure", task.getException());
////                            Toast.makeText(StartActivity.this, "Authentication failed.",
////                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//    }

    private boolean validateLinkForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) valid = false;

        if (TextUtils.isEmpty(password)) valid = false;

        return valid;
    }

}
