package com.example.mixit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mixit.R;
import com.example.mixit.activities.authentication.EmailPasswordActivity;
import com.example.mixit.activities.authentication.GoogleSignInActivity;
import com.example.mixit.services.authentication.GoogleAuth;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button mLoginButton, mGoogleButton, mFacebookButton, mEmailButton;

    private FirebaseAuth mAuth;

    GoogleAuth googleAuth;

    //public FragmentManager fm = getSupportFragmentManager();
    //public FragmentTransaction ft = fm.beginTransaction();
    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        switch (id){
//            case R.id.login_button:
//                intent = new Intent(this, LogInActivity.class);
//                break;
//            case R.id.anonymous_button:
//                intent = new Intent(this, AnonymousAuthActivity.class);
//                break;
            case R.id.google_button:
                googleAuth = new GoogleAuth(this);
                break;
            case R.id.facebook_button:
                break;
            case R.id.email_button:
                intent = new Intent(this, EmailPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    //public GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mLoginButton = findViewById(R.id.login_button);
        mGoogleButton = findViewById(R.id.google_button);
        mFacebookButton = findViewById(R.id.facebook_button);
        mEmailButton = findViewById(R.id.email_button);

        mLoginButton.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        mFacebookButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);

//  THE FOLLOWING CODE ALLOWS TO TELL IF THERE IS AN AUTHENTICATED USER AND PLACE A TOAST NOTIFICATION.
//  IT SHOULD BE USED TO DEFINED APP FLOW
//        if (currentUser != null) {
//            Toast.makeText(this, "Authenticated",Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Unauthenticated",Toast.LENGTH_LONG).show();
//        }
    }
}