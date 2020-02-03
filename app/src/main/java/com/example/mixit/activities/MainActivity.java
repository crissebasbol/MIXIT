package com.example.mixit.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mixit.R;
import com.example.mixit.fragments.ProfileFragment;
import com.example.mixit.fragments.main.ItemListFragment;
import com.example.mixit.fragments.main.ShowFragment;
import com.example.mixit.models.User;
import com.example.mixit.services.network.NetworkFunctions;
import com.example.mixit.utilities.ListViewAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends GenericAbstractActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ShowFragment.OnFragmentInteractionListener, ItemListFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private User user;

    private ListView listView;
    private ViewStub stubList;
    private ListViewAdapter listViewAdapter;
    private TextView mNameUser, mEmailUser;
    private int numberItems=10;
    private boolean add = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");

        boolean isConnected = NetworkFunctions.checkNetworkStatus(this);
        if (!isConnected) {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.message_no_internet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.txt_retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            startActivity(getIntent());
                        }
                    }).show();
        } else {
            ItemListFragment itemListFragment = new ItemListFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, itemListFragment);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Add the new tab fragment
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, ItemListFragment.newInstance())
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
        setTitle("");
        return super.onOptionsItemSelected(item);
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
        }
        // else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
        else if (id == R.id.nav_account) {
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void refreshNavigationDrawer(){
        setupGUINavigationDrawer();
    }
}
