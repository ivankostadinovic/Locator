package com.example.locator;

import org.xml.sax.Locator;

import java.util.ArrayList;

public class LocatorData {

    private ArrayList<Quest> doneQuests;
    private ArrayList<Quest> activeQuests;


    private LocatorData()
    {
        doneQuests=new ArrayList<>();
        activeQuests=new ArrayList<>();

    }


    private static class SingletonHolder
    {
        public static final LocatorData instance=new LocatorData();

    }
    public static LocatorData getInstance(){return SingletonHolder.instance;}

    public ArrayList<Quest> getDoneQuests() {return doneQuests;}

    public ArrayList<Quest> getActiveQuests() {return activeQuests;}

    public void addActive(Quest quest)
    {
        activeQuests.add(quest);
    }
    public void addDone(Quest quest)
    {
        doneQuests.add(quest);
    }
}
