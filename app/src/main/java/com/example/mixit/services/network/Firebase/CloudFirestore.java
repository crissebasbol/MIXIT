package com.example.mixit.services.network.Firebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mixit.R;
import com.example.mixit.fragments.CreateCockatilFragment;
import com.example.mixit.fragments.MyCocktailsFragment;
import com.example.mixit.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudFirestore {
    private FirebaseFirestore db;
    private Map<String, Object> dataToSave = new HashMap<>();
    private final static String TAG = "CloudFirestore";
    private  CreateCockatilFragment createCockatilFragment;
    private MyCocktailsFragment myCocktailsFragment;
    public final static String AUTHOR_KEY = "author";
    public final static String DATE_KEY = "date";
    public final static String TITLE_KEY = "title";
    public final static String DESCRIPTION_KEY = "description";
    public final static String TUTORIAL_KEY = "tutorial";
    public final static String IMAGE_URL_KEY = "image_url";
    public final static String INGREDIENTS_KEY = "ingredient_";
    public final static String QUANTITY_KEY = "quantity_";
    public final static String COCKTAIL_COLLECTION = "cocktails";

    private Activity activity;

    public CloudFirestore(@Nullable CreateCockatilFragment createCockatilFragment, @Nullable Activity activity, @Nullable MyCocktailsFragment myCocktailsFragment){
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        if (activity != null){
            this.activity = activity;
        }
        if (createCockatilFragment != null){
            this.createCockatilFragment = createCockatilFragment;
        }
        if (myCocktailsFragment != null){
            this.myCocktailsFragment = myCocktailsFragment;
        }
    }

    public void saveCocktail(List<EditText> ingredients, List<EditText> quantity,
                             String description, String tutorial, String urlImage, String author,
                             String title, @Nullable ProgressDialog progressDialog){
        Map<String, Object> cocktailToSave = new HashMap<>();
        cocktailToSave.put(AUTHOR_KEY, author);
        cocktailToSave.put(DATE_KEY, Calendar.getInstance().getTime());
        cocktailToSave.put(TITLE_KEY, title);
        cocktailToSave.put(DESCRIPTION_KEY, description);
        cocktailToSave.put(TUTORIAL_KEY, tutorial);
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
                            List<Item> items = new ArrayList<>();
                             for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v(TAG, "Information correctly retrieved: " + document.getId() + " => " + document.getData());
                                if (myCocktailsFragment != null){
                                    //String title, String imageUrl, String description, String instructions,
                                      //      String creatorsEmail, List<String> ingredients, List<String> quantity
                                    List<String> ingredients = new ArrayList<>();
                                    List<String> quantity = new ArrayList<>();
                                    int x = 1;
                                    while (x>0) {
                                        try {
                                            ingredients.add((String) document.getData().get(INGREDIENTS_KEY + x));
                                            quantity.add((String) document.getData().get(QUANTITY_KEY + x));
                                            x++;
                                            if (document.getData().get(INGREDIENTS_KEY + x) == null){
                                                throw new Exception("Index exceeded");
                                            }
                                        } catch (Exception e) {
                                            x = 0;
                                        }
                                    }
                                    items.add(new Item( (String) document.getId(),
                                            (String) document.getData().get("title"),
                                            (String) document.getData().get(IMAGE_URL_KEY),
                                            (String) document.getData().get(DESCRIPTION_KEY),
                                            (String) document.getData().get(TUTORIAL_KEY),
                                            (String) document.getData().get(AUTHOR_KEY),
                                            ingredients, quantity));
                                }
                            }
                            myCocktailsFragment.fillItems(items);
                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
