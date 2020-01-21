package com.example.mixit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.ListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mixit.R;
import com.example.mixit.interfaces.VolleyCallback;
import com.example.mixit.models.Item;
import com.example.mixit.services.network.JSONAPIRequest;
import com.example.mixit.utilities.ListViewAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, VolleyCallback {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ListView listView;
    private ViewStub stubList;
    private ListViewAdapter listViewAdapter;
    private List<Item> itemList = new ArrayList<>();
    private int numberItems=10;
    private boolean add = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //---------------------------
        stubList = (ViewStub) findViewById(R.id.stub_list);
        stubList.inflate();
        listView = (ListView) findViewById(R.id.my_list_view);
        setAdapters();
        //get list of product
        for(byte x=0;x<numberItems;x++){
            getItemList();
        }

    }

    private void setAdapters() {
        if (listViewAdapter == null) {
            listViewAdapter = new ListViewAdapter(this, R.layout.list_item, itemList);
            listView.setAdapter(listViewAdapter);
        }
        else {
            listViewAdapter.notifyDataSetChanged();
        }
        if (itemList.size()>numberItems-1){
            add = true;
        }
    }

    public void getItemList() {
        Item item = new Item();
        item.setId(1);
        item.setTitle("Margarita");
        item.setAlarm(null);
        item.setCreatorsEmail("crissebas@unicauca.edu.co");
        item.setDescription("Descripción...");
        item.setTutorial("Tutorial...");
        item.setPrepared(false);
        item.setImage(null);
        item.setFavourite(false);
        itemList.add(item);
        setAdapters();

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            finish();
            Intent startActivity = new Intent(this, StartActivity.class);
            startActivity(startActivity);
        }

        JSONAPIRequest APIService = new JSONAPIRequest(this, this);

        HashMap params = new HashMap();
        params.put("glass", null);
        params.put("alcohol", "Alcoholic");
        params.put("category", null);
        params.put("ingredient", null);

        HashMap query = new HashMap();
        query.put("type", "filter");
        query.put("params", params);

        APIService.execute(query);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
//            // Handle the camera action
        }
        // else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_tools) {
//
//        } else if (id == R.id.nav_share) {
//
//        }
        else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent startIntent = new Intent(this, StartActivity.class);
            startActivity(startIntent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSuccess(JSONArray response) {
        System.out.println("LENGTH ACTIVITY "+response.length());
        // TODO
    }
}
