package com.example.mixit.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

import java.util.concurrent.CompletableFuture;

public class JSONAPIRequest {

    private final String TAG = "JSONAPIRequest";
    private final String URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    private RequestQueue queue;

    public JSONAPIRequest (Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public CompletableFuture<JSONArray> filteredQuery (String glass, String alcohol, String category, String ingredient) {

        String queryURL = URL + "filter.php?";
        final JSONArray drinks = new JSONArray();

        if (glass != null) queryURL += "g=" + glass;
        if (alcohol != null) queryURL += "a=" + alcohol;
        if (category != null) queryURL += "c=" + category;
        if (ingredient != null) queryURL += "i=" + ingredient;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, queryURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray responseArray = response.getJSONArray("drinks");
                            for (int i = 0; i < responseArray.length(); i++) {
                                drinks.put(responseArray.getJSONObject(i));
                            }
                            await(drinks);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e(TAG, error.toString());
                    }
                });

        // Access the RequestQueue
        queue.add(jsonObjectRequest);

        return completedFuture(drinks);
    }
}
