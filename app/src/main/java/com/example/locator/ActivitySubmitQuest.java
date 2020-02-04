package com.example.locator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ActivitySubmitQuest extends ActivityBase {

    private Button btnSubmit;
    private EditText editName, editDesc, editLongitude, editLatitude;
    private RadioButton radioIstorijski, radioZabavni, radioIstrazivacki;
    private DatabaseReference db;
    Quest quest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_quest);

        initializeComponents();
    }

    @Override
    public void initializeComponents() {

        Toolbar toolbar =findViewById(R.id.toolbar);
        editName = findViewById(R.id.quest_name);
        editDesc = findViewById(R.id.quest_description);
        editLatitude = findViewById(R.id.latitude);
        editLongitude = findViewById(R.id.longitude);
        btnSubmit = findViewById(R.id.btn_submit_quest);
        radioIstorijski = findViewById(R.id.radio_istorijski);
        radioZabavni = findViewById(R.id.radio_zabavni);
        radioIstrazivacki = findViewById(R.id.radio_istrazivacki);
        quest = new Quest();
        db = FirebaseDatabase.getInstance().getReference();
        toolbar.setNavigationOnClickListener(v -> ActivitySubmitQuest.super.onBackPressed());

        btnSubmit.setOnClickListener(v -> addQuest());
    }

    public void getLocation(View view) {
        if (Tools.locationPermissionGiven(this)) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                editLongitude.setText(Double.toString(location.getLongitude()));
                editLatitude.setText(Double.toString(location.getLatitude()));
            });
        }
    }
    private void addQuest() {

        if (!emptyCheck(new EditText[]{editName, editDesc, editName}))
            return;
        for (QuestItem item : LocatorData.getInstance().itemsToAdd) {
            quest.addItem(item);
        }
        if (radioIstorijski.isSelected()) {
            quest.setType("Istorijski");
        } else if (radioIstrazivacki.isSelected()) {
            quest.setType("Istrazivacki");
        } else {
            quest.setType("Zabavni");
        }
        quest.setLatitude(Double.parseDouble(editLatitude.getText().toString()));
        quest.setLongitude(Double.parseDouble(editLongitude.getText().toString()));
        quest.setDescription(editDesc.getText().toString());
        quest.setName(editName.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        String todayDate = dateFormat.format(today);
        quest.setAddedOn(todayDate);
        LocatorData.getInstance().addQuest(quest, this);
        finish();

    }
}
