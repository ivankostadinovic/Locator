package com.example.locator;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestItem implements Serializable {

    public String id;
    public String name;
    public String location;
    public String image;
    public String question;
    public List<String> answers;
    public boolean correctAnswer;
    public boolean answered;
    public int answeredQuestion;

    public QuestItem() {
        answers = new ArrayList<>();
    }


}
