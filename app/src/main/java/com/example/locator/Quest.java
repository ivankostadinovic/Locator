package com.example.locator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quest implements Serializable {

    private List<Item> items;
    private String Name;
    private String Description;
    private int itemsFound;
    private String Id;
    private String Type;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setItemsFound(int itemsFound) {
        this.itemsFound = itemsFound;
    }

    public int getItemsFound() {
        return itemsFound;
    }

    public Quest(){

        items=new ArrayList<Item>();
    }
    public void addItem(Item item)
    {
        items.add(item);
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }
}
