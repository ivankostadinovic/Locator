package com.example.locator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

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
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMain extends ActivityBase implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mTextMessage;
    private FragmentQuests fragmentQuests;
    private FragmentMap fragmentMap;
    private FriendsFragment fragmentFriends;
    private GoogleSignInClient mGoogleSignInClient;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_quests:
                    openFragment(fragmentQuests);
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_friends:
                    openFragment(fragmentFriends);
                    return true;
                case R.id.navigation_map:

                    if (ContextCompat.checkSelfPermission(ActivityMain.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(ActivityMain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                    else {
                        openFragment(fragmentMap);
                    }
                    return true;
            }
            return false;
        }
    };

    private void openFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction
            .hide(fragmentQuests)
            .hide(fragmentMap)
            .hide(fragmentFriends)
            .show(fragment)
            .addToBackStack(null)
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
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
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
        fragmentMap = FragmentMap.newInstance(null);
        fragmentFriends = new FriendsFragment();
        commitFragments();
        openFragment(fragmentQuests);
    }

    public void commitFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
            .add(R.id.fragment_containter, fragmentMap)
            .add(R.id.fragment_containter, fragmentQuests)
            .add(R.id.fragment_containter, fragmentFriends)
            .hide(fragmentMap)
            .hide(fragmentFriends).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFragment(fragmentMap);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Tools.showMsg(this, "Please allow map permission.");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
