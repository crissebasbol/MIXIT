package com.example.mixit.activities.ui.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mixit.R;
import com.example.mixit.fragments.walkthrough.Walkthrough1Fragment;
import com.example.mixit.fragments.walkthrough.Walkthrough2Fragment;
import com.example.mixit.fragments.walkthrough.Walkthrough3Fragment;
import com.example.mixit.fragments.walkthrough.Walkthrough4Fragment;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public static Fragment newInstance(int index) {
        Fragment fragment = null;
        switch (index){
            case 1:
                fragment = new Walkthrough1Fragment();
                break;
            case 2:
                fragment = new Walkthrough2Fragment();
                break;
            case 3:
                fragment = new Walkthrough3Fragment();
                break;
            case 4:
                fragment = new Walkthrough4Fragment();
                break;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}