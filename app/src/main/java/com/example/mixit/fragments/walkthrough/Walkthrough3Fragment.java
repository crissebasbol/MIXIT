package com.example.mixit.fragments.walkthrough;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mixit.R;
import com.example.mixit.activities.StartActivity;
import com.example.mixit.activities.WalkthroughActivity;

public class Walkthrough3Fragment extends Fragment implements View.OnClickListener {

    Button skipButton;
    RadioButton mRadioButtonW1, mRadioButtonW2, mRadioButtonW3, mRadioButtonW4;
    RadioGroup radioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walkthrough3, container, false);
        skipButton = view.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(this);
        mRadioButtonW1 = view.findViewById(R.id.radioButtonW1);
        mRadioButtonW2 = view.findViewById(R.id.radioButtonW2);
        mRadioButtonW3 = view.findViewById(R.id.radioButtonW3);
        mRadioButtonW4 = view.findViewById(R.id.radioButtonW4);
        mRadioButtonW1.setOnClickListener(this);
        mRadioButtonW2.setOnClickListener(this);
        mRadioButtonW3.setOnClickListener(this);
        mRadioButtonW4.setOnClickListener(this);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        radioGroup.check(R.id.radioButtonW3);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        //((WalkthroughActivity)getActivity()).changeFragment(id);
        switch (id) {
            case R.id.skipButton:
                Intent intent = new Intent(getContext(), StartActivity.class);
                startActivity(intent);
                break;
            case R.id.radioButtonW1:
                ((WalkthroughActivity)getActivity()).changePage(1);
                break;
            case R.id.radioButtonW2:
                ((WalkthroughActivity)getActivity()).changePage(2);
                break;
            case R.id.radioButtonW3:
                ((WalkthroughActivity)getActivity()).changePage(3);
                break;
            case R.id.radioButtonW4:
                ((WalkthroughActivity)getActivity()).changePage(4);
                break;
        }
    }
}
