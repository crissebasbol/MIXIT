package com.example.mixit.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mixit.R;
import com.example.mixit.activities.authentication.SignInActivity;
import com.example.mixit.models.User;
import com.example.mixit.preferences.SessionPreferences;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class GenericAbstractActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SessionPreferences sessionPreferences;
    private NavigationView navigationView;
    private User user;
    private FireBaseAuth fireBaseAuth;
    private CircleImageView imageViewProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionPreferences = SessionPreferences.get(this, this);
        fireBaseAuth = new FireBaseAuth(this, this);
        checkAndSetupLogin();
    }

    private void checkAndSetupLogin() {
        if(!fireBaseAuth.checkSigned()){
            logOut();
        }
    }

    protected void setupGUINavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setLogo();
        //toolbar.setTitle();
        //toolbar.setTitleMarginStart();
        //setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView mTxtNameNavHeader =  header.findViewById(R.id.name_user);
        TextView mTxtEmailNavHeader = header.findViewById(R.id.email_user);
        mTxtNameNavHeader.setText(this.sessionPreferences.getCurrentUser().getFullName());
        mTxtEmailNavHeader.setText(this.sessionPreferences.getCurrentUser().getEmail());


        imageViewProfile = header.findViewById(R.id.profile_image);
        if (!sessionPreferences.getCurrentUser().getPhoto().equals("null")) {
            Picasso.get().load(sessionPreferences.getCurrentUser().getPhoto())
                    .resize(110, 110)
                    .centerCrop()
                    .into(imageViewProfile);
        }
        //TODO: Implement to get image
        for (int i = 0; i < this.navigationView.getMenu().size(); i++) {
            this.navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    protected void logOut() {
        fireBaseAuth.getmAuth().signOut();
        sessionPreferences.endSession();
        LoginManager.getInstance().logOut();
        Intent startIntent = new Intent(this, StartActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startIntent);
        finish();
    }

}
