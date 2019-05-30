package com.example.locator;

import java.util.ArrayList;

public class Quest {

    private ArrayList<Item> items;
    private String Name;
    private String Description;

    public Quest(){

        items=new ArrayList<>();
    }
    public void addItem(Item item)
    {
        items.add(item);
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }
}
