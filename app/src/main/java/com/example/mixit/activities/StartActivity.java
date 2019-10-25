package com.example.mixit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mixit.R;
import com.example.mixit.fragments.StartFragment;
import com.example.mixit.fragments.walkthrough.Walkthrough1Fragment;
import com.example.mixit.fragments.walkthrough.Walkthrough2Fragment;
import com.example.mixit.fragments.walkthrough.Walkthrough3Fragment;
import com.example.mixit.fragments.walkthrough.Walkthrough4Fragment;

public class StartActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    public Fragment walkthrough1, walkthrough2, walkthrough3, walkthrough4, startFragment;
    public FragmentManager fm = getSupportFragmentManager();
    public FragmentTransaction ft = fm.beginTransaction();
    public boolean firstTime = true;
    public GestureDetectorCompat mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (firstTime){
            walkthrough1 = new Walkthrough1Fragment();
            walkthrough2 = new Walkthrough2Fragment();
            walkthrough3 = new Walkthrough3Fragment();
            walkthrough4 = new Walkthrough4Fragment();
            startFragment = new StartFragment();
            ft.replace(R.id.fragmentStartActicity, walkthrough1);
            ft.commit();
        }else{

        }
        mDetector = new GestureDetectorCompat(this,this);
    }


    public void changeFragment(int fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (fragment){
            case R.id.skipButton:
                ft.replace(R.id.fragmentStartActicity, startFragment);
                break;
        }
        ft.commit();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result = false;
        float diffX = moveEvent.getX() - downEvent.getX();
        if (diffX>0){   //RIGHT
            Log.d("TAG", "RIGHT");
        }else if(diffX<0){      //LEFT
            Log.d("TAG", "LEFT");
        }
        return result;
    }

    /*ABSTRACT METHODS REQUIREDS FOR ONFLIG */
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
}