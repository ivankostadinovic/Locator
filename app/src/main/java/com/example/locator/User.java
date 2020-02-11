package com.example.locator;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class User implements Serializable {


    private String Id;
    private String Name;
    private String Password;
    private String Email;
    private String profilePicture;
    private double longitude;
    private double latitude;
    private int points;


    public User() {

    }

    public void setId(String id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmail() {
        return Email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof User && TextUtils.equals(((User) obj).getId(), (this.Id));
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
