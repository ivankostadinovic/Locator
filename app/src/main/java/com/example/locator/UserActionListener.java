package com.example.locator;

public interface UserActionListener {

    public void newFeedQuest(Quest quest);
    public void userLatitudeChagned(double latitude);
    public void userLongitudeChanged(double longitude);
    public void addedQuestListener(Quest quest);

}
