package com.example.locator;

public class Item {

    private String Id;
    private String Name;
    private String City;
    private String Location;


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
