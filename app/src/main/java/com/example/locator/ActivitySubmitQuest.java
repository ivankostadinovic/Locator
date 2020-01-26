package com.example.locator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ActivitySubmitQuest extends ActivityBase {

    private List<QuestItem> items;
    private Button btnSubmit;
    private EditText editName, editDesc;
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

        editName = findViewById(R.id.quest_name);
        editDesc = findViewById(R.id.quest_description);
        btnSubmit = findViewById(R.id.btn_submit_quest);
        radioIstorijski = findViewById(R.id.radio_istorijski);
        radioZabavni = findViewById(R.id.radio_zabavni);
        radioIstrazivacki = findViewById(R.id.radio_istrazivacki);
        quest = new Quest();
        db = FirebaseDatabase.getInstance().getReference();

        btnSubmit.setOnClickListener(v -> addQuest());

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
        quest.setDescription(editDesc.getText().toString());
        quest.setName(editName.getText().toString());
        LocatorData.getInstance().addQuest(quest, this);
        finish();

    }
}
