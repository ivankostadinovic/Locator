package com.example.locator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActivityQuestProgress extends ActivityBase implements View.OnClickListener {

    private TextView txtDate, txtName, txtDescription, txtQuestion, questProgress;
    private LinearLayout firstAnswer, secondAnswer, thirdAnswer;
    private List<TextView> answers = new ArrayList<>();
    private List<Button> pageButtons = new ArrayList<>(), outlineButtons = new ArrayList<>();
    private List<QuestItem> items = new ArrayList<>();
    private ImageView imageView;
    private List<ImageView> imageAnswers = new ArrayList<>();
    private int currentItemIndex = 0;
    private Quest quest;
    private int correctAnswerIndex = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_progress);
        quest = (Quest) getIntent().getSerializableExtra("quest");
        initializeComponents();

    }

    public void setUpPages(int index, boolean initial) {
        imageAnswers.get(0).setVisibility(View.INVISIBLE);
        imageAnswers.get(1).setVisibility(View.INVISIBLE);
        imageAnswers.get(2).setVisibility(View.INVISIBLE);

        if (currentItemIndex == index && !initial) {
            return;
        }
        pageButtons.get(currentItemIndex).setVisibility(View.VISIBLE);
        pageButtons.get(index).setVisibility(View.INVISIBLE);
        QuestItem selectedItem = items.get(index);
        imageView.setImageBitmap(Tools.StringToBitMap(quest.getItems().get(index).image));
        txtQuestion.setText(items.get(index).question);

        if (selectedItem.answered) {

            answers.get(0).setText(selectedItem.answers.get(0));
            answers.get(1).setText(selectedItem.answers.get(1));
            answers.get(2).setText(selectedItem.answers.get(2));
            if (!selectedItem.correctAnswer) {
                imageAnswers.get(selectedItem.answeredQuestion).setImageDrawable(getDrawable(R.drawable.ic_close_black_24dp));
                imageAnswers.get(2).setImageDrawable(getDrawable(R.drawable.ic_check_black_24dp));
                imageAnswers.get(selectedItem.answeredQuestion).setVisibility(View.VISIBLE);
            } else {
                imageAnswers.get(2).setImageDrawable(getDrawable(R.drawable.ic_check_black_24dp));
            }
            imageAnswers.get(2).setVisibility(View.VISIBLE);
            firstAnswer.setClickable(false);
            secondAnswer.setClickable(false);
            thirdAnswer.setClickable(false);
        } else {
            correctAnswerIndex = new Random().nextInt(3);
            answers.get(correctAnswerIndex).setText(selectedItem.answers.get(2));
            answers.get((correctAnswerIndex + 2) % 3).setText(selectedItem.answers.get(1));
            answers.get((correctAnswerIndex + 1) % 3).setText(selectedItem.answers.get(0));
            for (ImageView imageAnswer : imageAnswers) {
                imageAnswer.setVisibility(View.INVISIBLE);
            }
            firstAnswer.setClickable(true);
            secondAnswer.setClickable(true);
            thirdAnswer.setClickable(true);
        }
        currentItemIndex = index;
    }


    @Override
    public void initializeComponents() {

        txtDate = findViewById(R.id.date_added);
        txtName = findViewById(R.id.quest_name);
        txtDescription = findViewById(R.id.quest_description);
        txtQuestion = findViewById(R.id.item_question);
        questProgress = findViewById(R.id.quest_progress);
        imageView = findViewById(R.id.item_image);
        firstAnswer = findViewById(R.id.first_layout);
        secondAnswer = findViewById(R.id.second_layout);
        thirdAnswer = findViewById(R.id.third_layout);


        firstAnswer.setOnClickListener(this);
        secondAnswer.setOnClickListener(this);
        thirdAnswer.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> ActivityQuestProgress.super.onBackPressed());

        for (int i = 1; i < 4; i++) {
            TextView textView = findViewById(getResources().getIdentifier("item_answer_" + i, "id", getPackageName()));
            answers.add(textView);
        }
        for (int i = 1; i < 4; i++) {
            ImageView imageView = findViewById(getResources().getIdentifier("img_" + i, "id", getPackageName()));
            imageAnswers.add(imageView);
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

        questProgress.setText("Answered questions " + quest.getItemsFound() + "/" + quest.getItems().size());
        txtDate.setText(quest.getAddedOn());
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
            case R.id.first_layout:
                checkAnswer(0);
                break;
            case R.id.second_layout:
                checkAnswer(1);
                break;
            case R.id.third_layout:
                checkAnswer(2);
                break;
        }
    }

    private void checkAnswer(int i) {

        if (i == correctAnswerIndex) {
            Tools.showMsg(this, "Answer correct");
            items.get(currentItemIndex).answered = true;
            items.get(currentItemIndex).correctAnswer = true;
            quest.getItems().get(currentItemIndex).answered = true;
            quest.getItems().get(currentItemIndex).correctAnswer = true;
            imageAnswers.get(i).setImageDrawable(getDrawable(R.drawable.ic_check_black_24dp));
        } else {
            Tools.showMsg(this, "Answer incorrect");
            items.get(currentItemIndex).answered = true;
            items.get(currentItemIndex).correctAnswer = false;
            quest.getItems().get(currentItemIndex).answered = true;
            quest.getItems().get(currentItemIndex).correctAnswer = false;
            imageAnswers.get(i).setImageDrawable(getDrawable(R.drawable.ic_close_black_24dp));
            imageAnswers.get(correctAnswerIndex).setImageDrawable(getDrawable(R.drawable.ic_check_black_24dp));
            imageAnswers.get(correctAnswerIndex).setVisibility(View.VISIBLE);
        }
        imageAnswers.get(i).setVisibility(View.VISIBLE);

        questProgress.setText("Answered questions " + quest.getItemsFound() + "/" + quest.getItems().size());
        items.get(currentItemIndex).answeredQuestion = i;
        quest.getItems().get(currentItemIndex).answeredQuestion = i;
        quest.setItemsFound(quest.getItemsFound() + 1);

        LocatorData.getInstance().updateQuestProgress(quest);

        boolean allAnswered = true;
        int correctAnswers = 0;
        for (QuestItem item : items) {
            if (!item.answered) {
                allAnswered = false;
            } else if (item.correctAnswer) {
                correctAnswers++;
            }
        }
        if (allAnswered) {
            Tools.showMsg(this, "Quest finished with " + correctAnswers + " / " + items.size());
            setResult(RESULT_OK);
            LocatorData.getInstance().updateUserPoints(correctAnswers * 30);
            LocatorData.getInstance().finishQuest(quest);
        }
        firstAnswer.setClickable(false);
        secondAnswer.setClickable(false);
        thirdAnswer.setClickable(false);
    }
}
