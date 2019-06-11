package com.example.locator;

import android.graphics.Bitmap;

import java.util.List;

public class Item {

    private String Id;
    private String Name;
    private String City;
    private String Location;
    private List<Bitmap> Images;
    private Bitmap CapturedImage;

    public void setCapturedImage(Bitmap capturedImage) {
        CapturedImage = capturedImage;
    }

    public Bitmap getCapturedImage() {
        return CapturedImage;
    }

    public boolean isFound() {
        return Found;
    }

    public void setImages(List<Bitmap> images) {
        Images = images;
    }

    public List<Bitmap> getImages() {
        return Images;
    }

    public void setFound(boolean found) {
        Found = found;
    }
    public boolean getFound() {
        return Found;
    }

    private boolean Found;


    public Item()
    {

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

    public void setCity(String city) {
        City = city;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCity() {
        return City;
    }

    public String getLocation() {
        return Location;
    }


}
