package com.example.mixit.activities;

import android.os.Bundle;

import com.example.mixit.R;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mixit.activities.ui.start.SectionsPagerAdapter;
import com.example.mixit.services.authentication.FireBaseAuth;

public class WalkthroughActivity extends AppCompatActivity {
    public ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        //Check if a user signed in
        FireBaseAuth fireBaseAuth = new FireBaseAuth(this, this);
        if (fireBaseAuth.checkSigned()){
            fireBaseAuth.setMainActivity();
        }

        //adapters
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }

    public void changePage(int index){
        viewPager.setCurrentItem(index-1,true);
    }

}