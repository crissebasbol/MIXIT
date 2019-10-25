package com.example.mixit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mixit.R;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button mLoginButton, mGoogleButton, mFacebookButton, mEmailButton;
    //public FragmentManager fm = getSupportFragmentManager();
    //public FragmentTransaction ft = fm.beginTransaction();
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
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        switch (id){
            case R.id.login_button:
                intent = new Intent(this, LogInActivity.class);
                break;
            case R.id.google_button:
                break;
            case R.id.facebook_button:
                break;
            case R.id.email_button:
                break;
        }
        startActivity(intent);
    }
}