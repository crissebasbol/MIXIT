package com.example.mixit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mixit.R;

public class StartActivity extends AppCompatActivity {

    //public FragmentManager fm = getSupportFragmentManager();
    //public FragmentTransaction ft = fm.beginTransaction();
    //public GestureDetectorCompat mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

}