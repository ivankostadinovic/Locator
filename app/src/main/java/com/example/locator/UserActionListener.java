package com.example.locator;

public interface UserActionListener {

    public void newFeedQuest(Quest quest);
    public void addedQuestListener(Quest quest);
    public void friendLoadedListener(User friend);

    void removeQuest(Quest quest);
}
