package com.example.locator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ActivityMain extends ActivityBase implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentQuests fragmentQuests;
    private FragmentMap fragmentMap;
    private FragmentFriends fragmentFriends;
    private GoogleSignInClient mGoogleSignInClient;
    private Intent serviceIntent;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_quests:
                    openFragment(fragmentQuests);
                    return true;
                case R.id.navigation_friends:
                    openFragment(fragmentFriends);
                    return true;
                case R.id.navigation_map:
                    if (!Tools.locationPermissionGiven(ActivityMain.this)) {
                        ActivityCompat.requestPermissions(ActivityMain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                        return false;
                    } else {
                        if (fragmentMap == null) {
                            fragmentMap = FragmentMap.newInstance(null);
                            getSupportFragmentManager().beginTransaction().add(R.id.fragment_containter, fragmentMap).commit();
                        }
                        openFragment(fragmentMap);
                    }
                    return true;
            }
            return false;
        }
    };

    private void openFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragmentMap != null) {
            transaction.hide(fragmentMap);
        }
        transaction
            .hide(fragmentQuests)
            .hide(fragmentFriends)
            .show(fragment)
            .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityMain.this, ActivityAddQuest.class);
                startActivity(i);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE).getBoolean(Constants.LOCATION_SERVICE_ENABLED, true)) {
            serviceIntent = new Intent(this, LocatorService.class);
            startService(serviceIntent);
        }
    }

    @Override
    protected void onDestroy() {
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        super.onDestroy();
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
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile: {
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            }
            case R.id.nav_logout: {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .build();
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut();
                Intent i = new Intent(ActivityMain.this, ActivityStart.class);
                startActivity(i);
                break;
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_nav_bar);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentQuests = FragmentQuests.newInstance(null);
        if (Tools.locationPermissionGiven(ActivityMain.this)) {
            fragmentMap = FragmentMap.newInstance(null);
        }
        fragmentFriends = new FragmentFriends();
        commitFragments();
        openFragment(fragmentQuests);
    }

    public void commitFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
            .add(R.id.fragment_containter, fragmentQuests)
            .add(R.id.fragment_containter, fragmentFriends)
            .hide(fragmentFriends);
        if (fragmentMap != null) {
            transaction.add(R.id.fragment_containter, fragmentMap);
            transaction.hide(fragmentMap);
        }
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((BottomNavigationView) findViewById(R.id.bottom_nav_bar)).setSelectedItemId(R.id.navigation_map);
                } else {
                    Tools.showMsg(this, "Please allow map permission.");
                    return;
                }

            }
        }
    }
}