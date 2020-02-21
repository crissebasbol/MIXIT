package com.example.mixit.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.mixit.R;
import com.example.mixit.preferences.SessionPreferences;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.example.mixit.services.network.Firebase.CloudFirestore;
import com.example.mixit.services.network.Firebase.FB_Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateCockatilFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private View mView;
    private Button mAddIngredient, mBtnAddImage, mBtnSave;
    private ImageView addImage;
    private LinearLayout mLayoutIngredients;
    private List<EditText> mIngredients = new ArrayList<EditText>();
    private List<EditText> mQuantity = new ArrayList<EditText>();
    private EditText mDescription, mTitle, mTutorial;
    private static final Byte REQUEST_CAMERA = 1;
    private static final Byte SELECT_FILE = 0;
    public static final String NAME_FOLDER = "cocktails_photos";
    private Bitmap bitmapCocktailImage = null;

    public CreateCockatilFragment() {
        // Required empty public constructor
    }

    public static CreateCockatilFragment newInstance() {
        return new CreateCockatilFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.create_cocktail);
        mView = inflater.inflate(R.layout.fragment_create_cockatil, container, false);
        mLayoutIngredients = mView.findViewById(R.id.layout_ingredients);
        mIngredients.add((EditText) mView.findViewById(R.id.input_ingredient));
        mBtnAddImage = mView.findViewById(R.id.btn_add_image);
        addImage = mView.findViewById(R.id.add_image);
        mBtnSave = mView.findViewById(R.id.btn_save);
        mTitle = mView.findViewById(R.id.input_title_cocktail);
        mTutorial = mView.findViewById(R.id.cocktail_tutorial);
        mQuantity.add((EditText) mView.findViewById(R.id.input_quantity));
        mAddIngredient = mView.findViewById(R.id.add_ingredient);
        mAddIngredient.setOnClickListener(this);
        mBtnAddImage.setOnClickListener(this);
        addImage.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mDescription = mView.findViewById(R.id.cocktail_description);
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.add_ingredient){
            addLayoutIngredient();
        }
        else if (id == R.id.add_image || id == R.id.btn_add_image){
            selectImages();
        }
        else if (id == R.id.btn_save){
            if (validateForm())
                saveCocktail();
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        String title = mTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            mTitle.setError(getString(R.string.txt_required));
            valid = false;
        } else {
            mTitle.setError(null);
        }

        String description = mDescription.getText().toString();
        if (TextUtils.isEmpty(description)) {
            mDescription.setError(getString(R.string.txt_required));
            valid = false;
        } else {
            mDescription.setError(null);
        }

        String tutorial = mTutorial.getText().toString();
        if (TextUtils.isEmpty(tutorial)) {
            mTutorial.setError(getString(R.string.txt_required));
            valid = false;
        } else {
            mTutorial.setError(null);
        }

        if (bitmapCocktailImage == null){
            Toast.makeText(getContext(), R.string.txt_error_select_image, Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }

    private void addLayoutIngredient() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.item_ingredient, null);
        mIngredients.add((EditText) v.findViewById(R.id.input_ingredient));
        mQuantity.add((EditText) v.findViewById(R.id.input_quantity));
        mLayoutIngredients.addView(v);
    }

    private void saveCocktail(){
        System.out.println("Saving cocktail");
        FB_Storage fb_storage = new FB_Storage(getContext(), getActivity(), null, this);
        fb_storage.uploadFile(bitmapCocktailImage, mTitle.getText().toString(), NAME_FOLDER);
    }
    public void saveCocktailDB(String urlPhoto, @Nullable ProgressDialog progressDialog){
        CloudFirestore cloudFirestore = new CloudFirestore(this, getActivity(), null);
        FirebaseUser fu = FirebaseAuth.getInstance().getCurrentUser();
        cloudFirestore.saveCocktail(mIngredients, mQuantity, mDescription.getText().toString(),
                mTutorial.getText().toString(), urlPhoto, fu.getEmail(), mTitle.getText().toString(), progressDialog);
    }
    public void result(String data, boolean error){
        Toast.makeText(getContext(), data, Toast.LENGTH_LONG).show();
        if (!error){
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            MyCocktailsFragment myCocktailsFragment = new MyCocktailsFragment();
            fragmentTransaction.replace(R.id.frame_layout, myCocktailsFragment);
            fragmentTransaction.commit();
        }
    }
    private void selectImages() {
        final CharSequence[] items = {getString(R.string.txt_camera), getString(R.string.txt_gallery), getString(R.string.txt_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.txt_add_imagen_cocktail));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals(getString(R.string.txt_camera))){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }else if (items[which].equals(getString(R.string.txt_gallery))){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, getString(R.string.txt_select_photo)), SELECT_FILE);
                }else if (items[which].equals(getString(R.string.txt_cancel))){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //FB_Storage.activityResult(requestCode,resultCode,data);
        if (resultCode== Activity.RESULT_OK){
            bitmapCocktailImage = null;
            Uri selectedImage = null;
            if (requestCode == REQUEST_CAMERA){
                Bundle extras = data.getExtras();
                bitmapCocktailImage = (Bitmap) extras.get("data");
            }else if (requestCode == SELECT_FILE){
                selectedImage = data.getData();
                try {
                    bitmapCocktailImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmapCocktailImage != null) {
                addImage.setImageBitmap(bitmapCocktailImage);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
