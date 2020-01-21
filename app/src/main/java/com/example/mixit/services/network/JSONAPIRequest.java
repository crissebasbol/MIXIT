package com.example.mixit.services.network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mixit.activities.MainActivity;
import com.example.mixit.interfaces.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class JSONAPIRequest extends AsyncTask<HashMap, Void, JSONArray> {

    private final String TAG = "JSONAPIRequest";
    private static final String URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    private RequestQueue queue;
    private Context mContext;
    private VolleyCallback mCallback;

    // public JSONArray drinks = new JSONArray();

    public JSONAPIRequest (Context context, VolleyCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        this.queue = Volley.newRequestQueue(context);
    }

    private void performQuery (String queryURL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, queryURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray responseArray = response.getJSONArray("drinks");
                            mCallback.onSuccess(responseArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });

        // Access the RequestQueue
        queue.add(jsonObjectRequest);
    }

    @Override
    protected JSONArray doInBackground(HashMap... query) {
        String queryURL = null;
        HashMap params = null;

        switch (query[0].get("type").toString()) {
            case "filter":
                params = (HashMap) query[0].get("params");
                queryURL = URL + "filter.php?";

                if (params.get("glass") != null) queryURL += "g=" + params.get("glass");
                if (params.get("alcohol") != null) queryURL += "a=" + params.get("alcohol");
                if (params.get("category") != null) queryURL += "c=" + params.get("category");
                if (params.get("ingredient") != null) queryURL += "i=" + params.get("ingredient");

                performQuery(queryURL);
                break;
            case "random":
                queryURL = URL + "random.php";

                performQuery(queryURL);
                break;
            case "id":
                params = (HashMap) query[0].get("params");
                queryURL = URL + "lookup.php?i=" + params.get("ingredient");

                performQuery(queryURL);
                break;
        }
        return null;
    }
}
