package com.example.locator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ActivityQuestProgress extends ActivityBase implements View.OnClickListener {

    private TextView txtDate, txtName, txtDescription, txtQuestion;
    private List<TextView> answers = new ArrayList<>();
    private List<Button> pageButtons = new ArrayList<>(), outlineButtons = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private ImageView imageView;
    private int currentItem = 0;
    private Quest quest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_progress);

        quest = (Quest) getIntent().getSerializableExtra("quest");
        initializeComponents();


    }

    public void setUpPages(int index, boolean initial) {
        if (currentItem == index && !initial) {
            return;
        }
        pageButtons.get(currentItem).setVisibility(View.VISIBLE);
        pageButtons.get(index).setVisibility(View.INVISIBLE);

        imageView.setImageBitmap(StringToBitMap(quest.getItems().get(index).image));
        txtQuestion.setText(items.get(index).question);
        answers.get(0).setText(quest.getItems().get(index).answers.get(0));
        answers.get(1).setText(quest.getItems().get(index).answers.get(1));
        answers.get(2).setText(quest.getItems().get(index).answers.get(2));
        currentItem = index;
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

        for (int i = 1; i < 7; i++) {
            Button button = findViewById(getResources().getIdentifier("btn_page_" + i + "_outline", "id", getPackageName()));
            outlineButtons.add(button);
            Button button1 = findViewById(getResources().getIdentifier("btn_page_" + i, "id", getPackageName()));
            button1.setOnClickListener(this);
            if (items.size() > i) {
                button1.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
            pageButtons.add(button1);
            outlineButtons.add(button);
        }

        txtName.setText(quest.getName());
        txtDescription.setText(quest.getDescription());
        items.addAll(quest.getItems());
        setUpPages(0, true);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_page_1:
                setUpPages(0, false);
                break;
            case R.id.btn_page_2:
                setUpPages(1, false);
                break;
            case R.id.btn_page_3:
                setUpPages(2, false);
                break;
            case R.id.btn_page_4:
                setUpPages(3, false);
                break;
            case R.id.btn_page_5:
                setUpPages(4, false);
                break;
            case R.id.btn_page_6:
                setUpPages(5, false);
                break;
        }
    }
}
