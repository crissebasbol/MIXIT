package com.example.mixit.services.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mixit.activities.MainActivity;
import com.example.mixit.activities.StartActivity;
import com.example.mixit.activities.authentication.SignInActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookAuth {
    private static final String TAG = "FacebookAuth";
    private FirebaseAuth mAuth;
    private Context mContext;
    private Activity activity;
    private String urlPhoto;

    public FacebookAuth(Context context, FirebaseAuth mAuth, Activity activity){
        this.mContext = context;
        this.mAuth = mAuth;
        this.activity = activity;
    }
    public FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "onSuccess");
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v(TAG, response.toString());
                            try {
                                urlPhoto = "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //imagehttp://graph.facebook.com/2695999640436532/picture?type=large -- square, normal, album
                            //https://developers.facebook.com/docs/graph-api/reference/v2.2/user/picture
                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
            handleFacebookAccessToken(loginResult.getAccessToken());
        }
        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel");
        }
        @Override
        public void onError(FacebookException e) {
            Log.d(TAG, "onError " + e);
        }
    };

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FireBaseAuth fireBaseAuth = new FireBaseAuth(mContext, activity);
                            fireBaseAuth.updatePhotoUri(urlPhoto, StartActivity.class);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
