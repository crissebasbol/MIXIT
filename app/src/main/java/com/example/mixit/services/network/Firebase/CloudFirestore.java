package com.example.mixit.services.network.Firebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mixit.R;
import com.example.mixit.fragments.CreateCockatilFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudFirestore {
    private FirebaseFirestore db;
    private Map<String, Object> dataToSave = new HashMap<>();
    private final static String TAG = "CloudFirestore";
    private  CreateCockatilFragment createCockatilFragment;
    protected final static String AUTHOR_KEY = "author";
    protected final static String DATE_KEY = "date";
    protected final static String DESCRIPTION_KEY = "description";
    protected final static String IMAGE_URL_KEY = "image_url";
    protected final static String INGREDIENTS_KEY = "ingredient_";
    protected final static String QUANTITY_KEY = "quantity_";
    protected final static String COCKTAIL_COLLECTION = "cocktails";

    private Activity activity;

    public CloudFirestore(@Nullable CreateCockatilFragment createCockatilFragment, @Nullable Activity activity){
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        if (activity != null){
            this.activity = activity;
        }
        if (createCockatilFragment != null){
            this.createCockatilFragment = createCockatilFragment;
        }
    }

    public void saveCocktail(List<EditText> ingredients, List<EditText> quantity, String description, String urlImage, String author, @Nullable ProgressDialog progressDialog){
        Map<String, Object> cocktailToSave = new HashMap<>();
        cocktailToSave.put(AUTHOR_KEY, author);
        cocktailToSave.put(DATE_KEY, Calendar.getInstance().getTime());
        cocktailToSave.put(DESCRIPTION_KEY, description);
        cocktailToSave.put(IMAGE_URL_KEY, urlImage);
        for (int x=1; x<=ingredients.size(); x++){
            cocktailToSave.put(INGREDIENTS_KEY+x, ingredients.get(x-1).getText().toString());
            cocktailToSave.put(QUANTITY_KEY+x, quantity.get(x-1).getText().toString());
        }
        callCollection(COCKTAIL_COLLECTION, cocktailToSave, progressDialog);
    }

    public void callCollection(String collection, Map<String, Object> map, @Nullable final ProgressDialog progressDialog) {
        db.collection(collection)
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.v(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        progressDialog.dismiss();
                        if (createCockatilFragment != null){
                            createCockatilFragment.result(activity.getString(R.string.txt_congratulations_correct_cocktail_added));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        progressDialog.dismiss();
                        createCockatilFragment.result(activity.getString((R.string.txt_error_creating_cocktail)));
                    }
                });
    }

    public void getDocument(String collection, String author){
        db.collection(collection)
                .whereEqualTo(AUTHOR_KEY, author)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v(TAG, "Information correctly retrieved: " + document.getId() + " => " + document.getData());
                            }
                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
