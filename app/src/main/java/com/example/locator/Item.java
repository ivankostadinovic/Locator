package com.example.locator;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    public String id;
    public String name;
    public String hint;
    public String location;
    public String image;
    public String question;
    public List<String> answers;
    public Bitmap capturedImage;
    public boolean correctAnswer;
    public boolean answered;
    public int answeredQuestion;

    public Item() {
        answers = new ArrayList<>();
    }


}
