package com.example.mixit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mixit.R;
import com.example.mixit.activities.authentication.EmailPasswordActivity;
import com.example.mixit.services.authentication.AnonymousAuth;
import com.example.mixit.services.authentication.GoogleAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button mLoginButton, mGoogleButton, mFacebookButton, mEmailButton, mAnonymousButton;

    private FirebaseAuth mAuth;

    private GoogleAuth googleAuth;
    private AnonymousAuth anonymousAuth;

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
            case R.id.facebook_button:
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mLoginButton = findViewById(R.id.login_button);
        mGoogleButton = findViewById(R.id.google_button);
        mFacebookButton = findViewById(R.id.facebook_button);
        mEmailButton = findViewById(R.id.email_button);
        mAnonymousButton = findViewById(R.id.anonymous_button);

        mLoginButton.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        mFacebookButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);
        mAnonymousButton.setOnClickListener(this);

        //  THE FOLLOWING CODE ALLOWS TO TELL IF THERE'S AN AUTHENTICATED USER PRESENT.
        //  IT SHOULD BE USED TO DEFINED APP FLOW
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            finish();
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
    }
}
