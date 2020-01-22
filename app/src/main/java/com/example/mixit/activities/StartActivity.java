package com.example.mixit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mixit.R;
import com.example.mixit.activities.authentication.EmailPasswordActivity;
import com.example.mixit.services.authentication.AnonymousAuth;
import com.example.mixit.services.authentication.FacebookAuth;
import com.example.mixit.services.authentication.GoogleAuth;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button mLoginButton, mGoogleButton, mEmailButton, mAnonymousButton, mFacebookButton;

    private CallbackManager callbackManager;
    private LoginButton loginButtonFacebook;

    private FirebaseAuth mAuth;

    private GoogleAuth googleAuth;
    private AnonymousAuth anonymousAuth;
    private FacebookAuth facebookAuth;

    //public FragmentManager fm = getSupportFragmentManager();
    //public FragmentTransaction ft = fm.beginTransaction();

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        switch (id){
//            case R.id.login_button:
//                intent = new Intent(this, EmailPasswordActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.anonymous_button:
                anonymousAuth = new AnonymousAuth(this);
                break;
            case R.id.google_button:
                googleAuth = new GoogleAuth(this);
                break;
            case R.id.btnFa:
                loginButtonFacebook.performClick();
                facebookAuth = new FacebookAuth(this, mAuth, this);
                loginButtonFacebook.registerCallback(callbackManager, facebookAuth.mFacebookCallback);
                break;
            case R.id.email_button:
                intent = new Intent(this, EmailPasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("register", true);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
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
            //facebookAuth():
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Uncheck if you want to get your HASH
            //Utilities.printHashKey(this);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

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

        mAuth = FirebaseAuth.getInstance();

        //Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButtonFacebook = (LoginButton) findViewById(R.id.login_button_facebook);
        loginButtonFacebook.setPermissions(Arrays.asList("public_profile", "email"));
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            finish();
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
    }
}
