package com.example.locator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quest implements Serializable {

    private List<QuestItem> items;
    private String Name;
    private String Description;
    private int itemsFound;
    private String Id;
    private String Type;
    private Double longitude;
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

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

    public Quest() {

        items = new ArrayList<QuestItem>();
    }

    public void addItem(QuestItem item) {
        items.add(item);
    }

    public void setItems(List<QuestItem> items) {
        this.items = items;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<QuestItem> getItems() {
        return items;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }
}
