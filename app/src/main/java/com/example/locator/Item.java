package com.example.locator;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    private String Id;
    private String Name;
    private String Hint;
    private String Description;
    private String Location;
    private List<String> Images;
    private Bitmap CapturedImage;
    private boolean Found;

    public String getDescription() {
        return Description;
    }

    public void setCapturedImage(Bitmap capturedImage) {
        CapturedImage = capturedImage;
    }

    public Bitmap getCapturedImage() {
        return CapturedImage;
    }



    public void setImages(List<String > images) {
        Images = images;
    }

    public List<String> getImages() {
        return Images;
    }

    public void setFound(boolean found) {
        Found = found;
    }
    public boolean getFound() {
        return Found;
    }



    public void addImage(String image)
    {

            getImages().add(image);
    }


    public Item()
    {
        Images=new ArrayList<String >();
    }
    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }


    public void setLocation(String location) {
        Location = location;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getHint() {
        return Hint;
    }

    public String getLocation() {
        return Location;
    }


}
