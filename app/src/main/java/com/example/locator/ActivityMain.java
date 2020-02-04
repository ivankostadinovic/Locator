package com.example.locator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;

public class ActivityMain extends ActivityBase implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentQuests fragmentQuests;
    private FragmentMap fragmentMap;
    private FragmentFriends fragmentFriends;
    private final int REQUEST_CODE_SERVICE = 555;

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
        floatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(ActivityMain.this, DiscoverFriendsActivity.class);
            startActivity(i);
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
        if (!sharedPrefs.getServiceDialogShown()) {
            showServiceDialog();
            sharedPrefs.saveServiceDialogShown(true);
        } else if (sharedPrefs.getServiceEnabled()) {
            setUpServiceAndWorker();
        }
        LocatorData.getInstance().loadFriends();
    }

    private void showServiceDialog() {
        new MaterialAlertDialogBuilder(this)
            .setTitle("Location service")
            .setMessage("Do you want to send your location periodically?")
            .setPositiveButton("Yes", (dialog, which) -> {
                setUpServiceAndWorker();
                dialog.dismiss();
            }).setNegativeButton("No", (dialog, which) -> {
            sharedPrefs.putServiceEnabled(false);
            dialog.dismiss();
        }).show();

    }

    private void setUpServiceAndWorker() {
        if (Tools.locationPermissionGiven(this)) {
            sharedPrefs.putServiceEnabled(true);
            startService(new Intent(this, LocatorService.class));
            Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

            PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(LocatorWorker.class, 15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork("locator", ExistingPeriodicWorkPolicy.REPLACE, saveRequest);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_SERVICE);
        }
    }

    private void stopServiceAndWorker() {
        stopService(new Intent(this, LocatorService.class));
        WorkManager.getInstance(this).cancelUniqueWork("locator");
    }

    //servis se gasi u pozadini posle najvise par minuta tako da pri izlasku iz aplikacije moze da se iskljuci, za rad u pozadini sluzi LocatorWorker
    @Override
    protected void onDestroy() {
        stopService(new Intent(this, LocatorService.class));
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
                sharedPrefs.clear();
                stopServiceAndWorker();
                Intent i = new Intent(this, ActivityLoginEmail.class);
                startActivity(i);
                break;
            }
            case R.id.nav_settings: {
                startActivity(new Intent(this, ActivitySettings.class));
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
            case REQUEST_CODE_SERVICE: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpServiceAndWorker();
                } else {
                    Tools.showInfoDialog(this, "Need location permission to start service.\nYou can enable this later in settings.");
                }
            }
        }
    }


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
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                        return false;
                    } else {
                        if (fragmentMap == null) {
                            fragmentMap = FragmentMap.newInstance(null);
                            getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragment_containter, fragmentMap)
                                .commit();
                        }
                        openFragment(fragmentMap);
                    }
                    return true;
            }
            return false;
        }
    };
}