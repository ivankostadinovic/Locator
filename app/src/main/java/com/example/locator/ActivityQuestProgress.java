package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ActivityQuestProgress extends ActivityBase {

    private TextView txtDate, txtName, txtDescription, txtQuestion;
    private List<TextView> answers = new ArrayList<>();
    private ImageView imageView;
    Quest quest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_progress);

        initializeComponents();
        quest = (Quest) getIntent().getSerializableExtra("quest");
        txtName.setText(quest.getName());
        txtDescription.setText(quest.getDescription());
        imageView.setImageBitmap(StringToBitMap(quest.getItems().get(0).image));
        txtQuestion.setText(quest.getItems().get(0).question);

        answers.get(0).setText(quest.getItems().get(0).answers.get(0));
        answers.get(1).setText(quest.getItems().get(0).answers.get(1));
        answers.get(2).setText(quest.getItems().get(0).answers.get(2));


    }

    @Override
    public void initializeComponents() {

        txtDate = findViewById(R.id.date_added);
        txtName = findViewById(R.id.quest_name);
        txtDescription = findViewById(R.id.quest_description);
        txtQuestion = findViewById(R.id.item_question);
        imageView = findViewById(R.id.img_1);

        for (int i = 1; i < 4; i++) {
            TextView editText = findViewById(getResources().getIdentifier("item_answer_" + i, "id", getPackageName()));
            answers.add(editText);
        }

    }
}
