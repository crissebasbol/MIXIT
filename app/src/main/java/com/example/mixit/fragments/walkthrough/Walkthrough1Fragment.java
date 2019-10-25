package com.example.mixit.fragments.walkthrough;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mixit.R;
import com.example.mixit.activities.StartActivity;

public class Walkthrough1Fragment extends Fragment implements View.OnClickListener {
    Button skipButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walkthrough1, container, false);
        skipButton = view.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        ((StartActivity)getActivity()).changeFragment(id);
    }
}
