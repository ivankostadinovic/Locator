package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ActivitySubmitQuest extends ActivityBase {

    private List<Item> items;
    private Button btnSubmit;
    private EditText editName,editDesc;
    private RadioButton radioIstorijski,radioZabavni,radioIstrazivacki;
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

        editName=(EditText) findViewById(R.id.quest_name);
        editDesc=(EditText) findViewById(R.id.quest_description);
        btnSubmit=(Button) findViewById(R.id.btn_submit_quest);
        radioIstorijski=(RadioButton)  findViewById(R.id.radio_istorijski);
        radioZabavni=(RadioButton)  findViewById(R.id.radio_zabavni);
        radioIstrazivacki=(RadioButton)  findViewById(R.id.radio_istrazivacki);
        items=new ArrayList<Item>();
        quest=new Quest();
        items=(List<Item>)getIntent().getExtras().getSerializable("Items");
        db=FirebaseDatabase.getInstance().getReference();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuest();
            }
        });
        
    }

    private void addQuest()
    {
        List<Item> ok=new ArrayList<Item>();
        for(int i=0;i<3;i++)
            ok.add(items.get(i));
        quest.setDescription(editDesc.getText().toString());
        quest.setName(editName.getText().toString());
        quest.setItems(ok);
        String key= db.child("Feed-quests").push().getKey();
        db.child("Quests").child("Feed-quests").child(key).setValue(quest);
        db.child("Quests").child("Added-quests").child(LocatorData.getInstance().getUser().getId()).setValue(key);
    }
}
