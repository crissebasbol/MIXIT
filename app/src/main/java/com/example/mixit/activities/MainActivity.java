package com.example.mixit.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mixit.R;
import com.example.mixit.fragments.ProfileFragment;
import com.example.mixit.fragments.main.ItemListFragment;
import com.example.mixit.fragments.main.ShowFragment;
import com.example.mixit.interfaces.VolleyCallback;
import com.example.mixit.models.Item;
import com.example.mixit.services.network.JSONAPIRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends GenericAbstractActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ShowFragment.OnFragmentInteractionListener, ItemListFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, VolleyCallback {

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private MenuItem back;

    private ItemListFragment itemListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();
        itemListFragment = new ItemListFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, itemListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        setContentView(R.layout.activity_main);
        setupGUINavigationDrawer();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("");

//        boolean isConnected = NetworkFunctions.checkNetworkStatus(this);
//        if (!isConnected) {
//            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "No Internet connection detected", Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Retry", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    }).show();
//        } else {
//
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            returnToFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_settings);

        back = menu.findItem(R.id.action_back);
        back.setVisible(false);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(itemListFragment);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        returnToFragment();
        return super.onOptionsItemSelected(item);
    }

    private void returnToFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Add the new tab fragment
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, itemListFragment)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
        setTitle("");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            ItemListFragment itemListFragment = new ItemListFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, itemListFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_surprise) {
            fetchRandomItem();
        } else if (id == R.id.nav_account) {
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, profileFragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_privacy_terms) {
            Intent intent = new Intent(this, TermsAndConditionsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
            logOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void setBackButtonVisibility (boolean visible) {
        if (back != null) back.setVisible(visible);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void refreshNavigationDrawer(){
        setupGUINavigationDrawer();
    }

    public void fetchRandomItem () {
        JSONAPIRequest APIService = new JSONAPIRequest(this, this);

        HashMap query = new HashMap();
        query.put("type", "random");
        query.put("showFloating", true);

        APIService.execute(query);
    }

    @Override
    public void onSuccess(JSONArray response) {
        try {
            Item item = new Item((JSONObject) response.get(0));
            ShowFragment showFragment = new ShowFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", item);
            bundle.putBoolean("showFloating", true);
            showFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, showFragment);
            fragmentTransaction.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannel () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "notificationChannel";
            String description = "Channel for notification reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notificationChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
