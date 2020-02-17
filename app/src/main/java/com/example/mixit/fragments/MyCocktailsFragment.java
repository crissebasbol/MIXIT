package com.example.mixit.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;

import com.example.mixit.R;
import com.example.mixit.models.Item;
import com.example.mixit.services.network.Firebase.CloudFirestore;
import com.example.mixit.utilities.ListViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCocktailsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private View mView;
    private FloatingActionButton createCocktailButton;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ListView listView;
    private ListViewAdapter listViewAdapter = null;
    private FragmentManager mFragmentManager;
    private List<Item> itemList = new ArrayList<>();

    public static final String TAG = "MyCocktailsFragment";

    public MyCocktailsFragment() {
        // Required empty public constructor
    }

    public static MyCocktailsFragment newInstance(String param1, String param2) {
        /*
        MyCocktailsFragment fragment = new MyCocktailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return new MyCocktailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getActivity().getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_my_cocktails, container, false);
            createCocktailButton = mView.findViewById(R.id.fab_button_create_cocktail);
            createCocktailButton.setOnClickListener(this);
            getActivity().setTitle(R.string.txt_my_cocktails);
            ViewStub stubList = mView.findViewById(R.id.stub_list);
            stubList.inflate();
            listView = mView.findViewById(R.id.my_list_view);
            //listView.setOnScrollListener(this);


            CloudFirestore cloudFirestore = new CloudFirestore(null, getActivity());
            cloudFirestore.getDocument(CloudFirestore.COCKTAIL_COLLECTION, user.getEmail());
        }
        return mView;
    }

    private void setAdapters() {
        if (listViewAdapter == null) {
            listViewAdapter = new ListViewAdapter(mFragmentManager, getContext(), R.layout.list_item, itemList);
            listView.setAdapter(listViewAdapter);
        } else {
            listViewAdapter.notifyDataSetChanged();
        }
    }

    public void fillItems(){

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fab_button_create_cocktail){
            CreateCockatilFragment createCockatilFragment = new CreateCockatilFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, createCockatilFragment);
            fragmentTransaction.commit();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
