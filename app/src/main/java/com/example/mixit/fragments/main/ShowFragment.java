package com.example.mixit.fragments.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.mixit.R;
import com.example.mixit.activities.MainActivity;
import com.example.mixit.interfaces.UpdateCallback;
import com.example.mixit.models.Item;
import com.example.mixit.services.assets.BlurImages;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowFragment extends Fragment implements UpdateCallback, Button.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Item mItem;
    private View mView;
    private Context mContext;
    private ImageView picture;
    private TextView description, tutorial, ingredients;
    private static final String TAG = "ShowFragment";
    private FloatingActionButton mRandom;
    private boolean showFloating = false;

    private OnFragmentInteractionListener mListener;

    public ShowFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ShowFragment newInstance() {
        ShowFragment fragment = new ShowFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            this.mItem = (Item) getArguments().getSerializable("item");
            this.mContext = getActivity();
            ((MainActivity) this.mContext).setBackButtonVisibility(true);
            this.showFloating = getArguments().getBoolean("showFloating", false);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_show, container, false);
        mRandom = mView.findViewById(R.id.random);
        if (showFloating) {
            mRandom.setOnClickListener(this);
        } else {
            mRandom.setVisibility(View.INVISIBLE);
        }

        DisplayMetrics display = getResources().getDisplayMetrics();
        int width = display.widthPixels;
        picture = mView.findViewById(R.id.cocktail_picture);
        picture.getLayoutParams().height =(int) (width*0.6);
        description = mView.findViewById(R.id.cocktail_description);
        tutorial = mView.findViewById(R.id.cocktail_tutorial);
        ingredients = mView.findViewById(R.id.cocktail_ingredients);
        description.setText(this.mItem.getDescription());
        tutorial.setText(this.mItem.getTutorial().get("instructions").toString());
        ingredients.setText(this.mItem.getTutorial().get("ingredients").toString());
        if (this.mItem.getImage() == null) {
            this.mItem.setUpdateCallback(this);
        } else {
            picture.setImageBitmap(this.mItem.getImage());
            Drawable drawablePicture = new BitmapDrawable(getResources(), BlurImages.blur(this.mContext, this.mItem.getImage()));
            picture.setBackground(drawablePicture);
        }
        try {
            tutorial.setText(this.mItem.getTutorial().get("instructions").toString());
            ingredients.setText(this.mItem.getTutorial().get("ingredients").toString());
        }catch (Exception e){
            Log.w(TAG, "Error: "+e);
        }
        picture.setImageBitmap(this.mItem.getImage());
        Drawable drawablePicture = new BitmapDrawable(getResources(), BlurImages.blur(this.mContext, this.mItem.getImage()));
        picture.setBackground(drawablePicture);
        getActivity().setTitle(this.mItem.getTitle());
        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    public void onUpdate(@Nullable String itemId) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
            mView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
            ImageView picture = mView.findViewById(R.id.cocktail_picture);
            picture.setImageBitmap(mItem.getImage());
            picture.setImageBitmap(mItem.getImage());
            Drawable drawablePicture = new BitmapDrawable(getResources(),
                    BlurImages.blur(mContext, mItem.getImage()));
            picture.setBackground(drawablePicture);
            }
        });
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) mContext).fetchRandomItem();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
