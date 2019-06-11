package com.example.locator;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

public class ActivityMain extends ActivityBase {
    private TextView mTextMessage;
    private FragmentAddQuest fragmentAddQuest;
    private FragmentQuests fragmentQuests;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_add_quest:
                    openFragment(fragmentAddQuest);
                    return true;
                case R.id.navigation_quests:
                    openFragment(fragmentQuests);
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_friends:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    private void openFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        //    transaction.setCustomAnimations(R.animator.slide_in_left,
                 //   R.animator.slide_out_right, 0, 0);
        transaction.replace(R.id.fragment_containter, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
    }

    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_nav_bar);
        //mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentAddQuest=FragmentAddQuest.newInstance(null);
        fragmentQuests=FragmentQuests.newInstance(null);

        openFragment(fragmentAddQuest);
    }
}
