package com.example.mixit.services.storage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mixit.R;
import com.example.mixit.activities.MainActivity;
import com.example.mixit.fragments.ProfileFragment;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import kotlin.coroutines.experimental.Continuation;


public class FB_Storage {

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Activity activity;
    private Context context;
    private ProfileFragment profileFragment;
    private static final String TAG = "FB_Storage";

    public FB_Storage(Context context, Activity activity, @Nullable  ProfileFragment profileFragment){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        this.context = context;
        this.activity = activity;
        this.profileFragment = profileFragment;
    }

    public void uploadFile(Bitmap bitmap, final String namePicture){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(activity.getString(R.string.txt_loading_update));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final StorageReference profileRed = storageRef.child("profile_photos/"+namePicture+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRed.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.v(TAG,"Update photo succesfull");
                getUrlPhoto(namePicture, progressDialog);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v(TAG,"Update photo ERROR: "+e);
                progressDialog.dismiss();
                Toast.makeText(context, R.string.txt_error_update, Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //progressDialog.setMessage((int)progress+activity.getString(R.string.txt_load));
            }
        });
    }

    public void getUrlPhoto(String namePicture, final ProgressDialog progressDialog){
        final StorageReference profileRed = storageRef.child("profile_photos/"+namePicture+".jpg");
        profileRed.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri){
                String urlPhoto = uri.toString();
                if (profileFragment != null) {
                    profileFragment.updateProfile(urlPhoto, progressDialog);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(context, R.string.txt_error_update, Toast.LENGTH_LONG).show();
            }
        });

    }

}
