package com.example.locator;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    public String Id;
    public String Name;
    public String Hint;
    public String Description;
    public String Location;
    public List<String> Images;
    public Bitmap CapturedImage;
    public boolean Found;


}
