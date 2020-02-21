package com.example.mixit.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.mixit.R;
import com.example.mixit.preferences.SessionPreferences;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.example.mixit.services.network.Firebase.FB_Storage;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private Context mContext;
    private FragmentManager mFragmentManager;
    private SessionPreferences sessionPreferences;
    private AutoCompleteTextView mNameField;
    private EditText mPasswordField, mPasswordField2;
    private TextView mEmailTxt;
    private CircleImageView mProfilePicture;
    private View mView;
    private Button updateProfileBtn;
    private RelativeLayout mProfileImageLayout;
    private FireBaseAuth fireBaseAuth;
    private OnFragmentInteractionListener mListener;
    private Bitmap bitmapProfileImage = null;
    private boolean changeProfileImage = false;
    private FB_Storage fb_storage;
    public static final int RC_PROFILE_FRAGMENT = 8001;
    private static final Byte REQUEST_CAMERA = 1;
    private static final Byte SELECT_FILE = 0;
    public static final Byte UPDATE_PROFILE = 2;
    public static final String NAME_FOLDER = "profile_photos";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
        sessionPreferences = SessionPreferences.get(mContext, getActivity(), null);
        fireBaseAuth = new FireBaseAuth(mContext, getActivity());
        this.mFragmentManager = ((Activity) mContext).getFragmentManager();
        fb_storage = new FB_Storage(mContext, getActivity(), this, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_profile, container, false);
        mNameField = mView.findViewById(R.id.input_name);
        mPasswordField = mView.findViewById(R.id.password1);
        mPasswordField2 = mView.findViewById(R.id.password2);
        mEmailTxt = mView.findViewById(R.id.txt_email);
        mProfilePicture = mView.findViewById(R.id.profile_image);
        updateProfileBtn = mView.findViewById(R.id.btn_update_profile);
        mProfileImageLayout = mView.findViewById(R.id.profile_image_layout);

        mProfileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImages();
            }
        });

        mNameField.setText(sessionPreferences.getCurrentUser().getFullName());
        mEmailTxt.setText(sessionPreferences.getCurrentUser().getEmail());
        if (!sessionPreferences.getCurrentUser().getPhoto().equals("null")) {
            Picasso.get().load(sessionPreferences.getCurrentUser().getPhoto())
                    .resize(110, 110)
                    .centerCrop()
                    .into(mProfilePicture);
        }

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeProfileImage){
                    fb_storage.uploadFile(bitmapProfileImage, "pictureOf_"+sessionPreferences.getCurrentUser().getEmail(), NAME_FOLDER);
                    changeProfileImage = false;
                }else {
                    updateProfile(sessionPreferences.getCurrentUser().getPhoto(), null);
                }
            }
        });

        return mView;
    }

    private void selectImages() {
        final CharSequence[] items = {getString(R.string.txt_camera), getString(R.string.txt_gallery), getString(R.string.txt_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.txt_title_change_profile_picture));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals(getString(R.string.txt_camera))){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }else if (items[which].equals(getString(R.string.txt_gallery))){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    /*
                    try {
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                    }catch (Exception e){}
                     */
                    startActivityForResult(intent.createChooser(intent, getString(R.string.txt_select_photo)), SELECT_FILE);
                }else if (items[which].equals(getString(R.string.txt_cancel))){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void updateProfile(String urlPhoto, @Nullable ProgressDialog progressDialog){
        updateProfile(mNameField.getText().toString(), urlPhoto, mPasswordField.getText().toString(), mPasswordField2.getText().toString(), progressDialog);
    }
    private void updateProfile(String name, String photo, String password1, String password2, @Nullable ProgressDialog progressDialog) {
        if (!validateForm(name, password1, password2)){
            return;
        }

        if (!password1.equals("")){
            fireBaseAuth.updateProfile(name, photo, password1, progressDialog);
        }else{
            fireBaseAuth.updateProfile(name, photo, null, progressDialog);
        }
    }

    private boolean validateForm(String name, String password1, String password2) {
        boolean valid = true;
        if (TextUtils.isEmpty(name)) {
            mNameField.setError(getString(R.string.txt_required));
            valid = false;
        } else {
            mNameField.setError(null);
        }
        if (!TextUtils.isEmpty(password1)) {
            if (password1.length()<6){
                mPasswordField.setError(getString(R.string.txt_min_characteres));
                valid = false;
            }
            else {
                if (TextUtils.isEmpty(password2)) {
                    mPasswordField2.setError(getString(R.string.txt_required));
                    valid = false;
                } else {
                    mPasswordField2.setError(null);
                }
                if (!password1.equals(password2)) {
                    mPasswordField2.setError(getString(R.string.txt_error_same_passwords));
                    valid = false;
                } else {
                    mPasswordField2.setError(null);
                }
            }
        }
        return valid;
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //FB_Storage.activityResult(requestCode,resultCode,data);
        if (resultCode==Activity.RESULT_OK){
            bitmapProfileImage = null;
            Uri selectedImage = null;
            if (requestCode == REQUEST_CAMERA){
                Bundle extras = data.getExtras();
                bitmapProfileImage = (Bitmap) extras.get("data");
            }else if (requestCode == SELECT_FILE){
                selectedImage = data.getData();
                try {
                    bitmapProfileImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmapProfileImage != null) {
                mProfilePicture.setImageBitmap(bitmapProfileImage);
                changeProfileImage = true;
                //fb_storage.uploadFile(bitmapProfileImage);
            }
        }
    }

}
