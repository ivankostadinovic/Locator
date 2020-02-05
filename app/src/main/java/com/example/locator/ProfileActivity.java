package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user;
        String friendId = getIntent().getStringExtra("friend_id");
        if (friendId != null) {
            user = LocatorData.getInstance().getFriendLocal(getIntent().getStringExtra("friend_id"));
        } else {
            user = LocatorData.getInstance().getUser();
        }

        new Thread(() -> ((ImageView) findViewById(R.id.image)).setImageBitmap(Tools.StringToBitMap(user.getImage()))).start();
        ((TextView) findViewById(R.id.name)).setText(user.getName());
        ((TextView) findViewById(R.id.email)).setText(user.getEmail());
        ((TextView) findViewById(R.id.points)).setText(String.valueOf(user.getPoints()));
        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
    }
}
