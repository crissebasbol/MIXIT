package com.example.mixit.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mixit.R;
import com.example.mixit.activities.authentication.CreateAccountActivity;
import com.example.mixit.activities.authentication.SignInActivity;
import com.example.mixit.models.User;
import com.example.mixit.preferences.SessionPreferences;
import com.example.mixit.services.authentication.AnonymousAuth;
import com.example.mixit.services.authentication.FacebookAuth;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.example.mixit.services.authentication.GoogleAuth;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;


public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLoginButton, mGoogleButton, mEmailButton, mAnonymousButton, mFacebookButton;

    private TextView mTermsConditions;

    private CallbackManager callbackManager;
    private LoginButton loginButtonFacebook;

    private GoogleAuth googleAuth;
    private AnonymousAuth anonymousAuth;
    private FacebookAuth facebookAuth;
    private FireBaseAuth fireBaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mTermsConditions = findViewById(R.id.terms_conditions);

        mLoginButton = findViewById(R.id.login_button);
        mGoogleButton = findViewById(R.id.google_button);
        mEmailButton = findViewById(R.id.email_button);
        mAnonymousButton = findViewById(R.id.anonymous_button);
        mFacebookButton = (Button) findViewById(R.id.btnFa);

        mLoginButton.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);
        mAnonymousButton.setOnClickListener(this);
        mFacebookButton.setOnClickListener(this);
        mTermsConditions.setOnClickListener(this);

        fireBaseAuth = new FireBaseAuth(this, this);

        //Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButtonFacebook = (LoginButton) findViewById(R.id.login_button_facebook);
        loginButtonFacebook.setPermissions(Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onStart() {
        super.onStart();
        //Uncheck if you want to get your HASH
        //Utilities.printHashKey(this);
        // Check if user is signed in (non-null) and update UI accordingly.
        if(fireBaseAuth.checkSigned()){
            String email = fireBaseAuth.getmAuth().getCurrentUser().getEmail();
            String name = fireBaseAuth.getmAuth().getCurrentUser().getDisplayName();
            String photo = String.valueOf(fireBaseAuth.getmAuth().getCurrentUser().getPhotoUrl());
            User user = new User(email, name, photo);
            SessionPreferences sessionPreferences = new SessionPreferences(this, this, null);
            sessionPreferences.saveSessionUser(user);
            fireBaseAuth.setMainActivity();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        switch (id){
            case R.id.login_button:
                intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.anonymous_button:
                anonymousAuth = new AnonymousAuth(this);
                break;
            case R.id.google_button:
                googleAuth = new GoogleAuth(this);
                break;
            case R.id.btnFa:
                loginButtonFacebook.performClick();
                facebookAuth = new FacebookAuth(this, fireBaseAuth.getmAuth(), this);
                loginButtonFacebook.registerCallback(callbackManager, facebookAuth.mFacebookCallback);
                break;
            case R.id.email_button:
                intent = new Intent(this, CreateAccountActivity.class);
                //Bundle bundle = new Bundle();
                //bundle.putBoolean("register", true);
                //intent.putExtras(bundle);
                startActivity(intent);
                //finish();
                break;
            case R.id.terms_conditions:
                intent = new Intent(this, TermsAndConditionsActivity.class);
                startActivity(intent);
        }
    }

    //public GestureDetectorCompat mDetector;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoogleAuth.RC_SIGN_IN) {
            googleAuth.authenticate(data);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
