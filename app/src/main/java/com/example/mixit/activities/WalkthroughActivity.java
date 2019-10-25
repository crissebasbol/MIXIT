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

public class WalkthroughActivity extends AppCompatActivity {
    public ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        //adapters
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }

    public void changePage(int index){
        viewPager.setCurrentItem(index-1,true);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem()+i;
    }
}