package com.example.locator;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class LocatorData {

    private ArrayList<Quest> doneQuests;
    private ArrayList<Quest> activeQuests;
    public List<Item> itemsToAdd;
    private User user;
    private DatabaseReference db;


    private LocatorData() {
        doneQuests = new ArrayList<>();
        activeQuests = new ArrayList<>();
        user = new User();
        db = FirebaseDatabase.getInstance().getReference();

    }

    public void feedQuestListener(final FeedsListFragment feedsListFragment) {
        db.child("Quests").child("Feed-quests").addChildEventListener(new ChildEventListener() {// za ucitvaanje feed questova

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Quest quest = dataSnapshot.getValue(Quest.class);
               // feedsListFragment.addQuest(quest);

                Tools.log("heeloo");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadAddedQuests(final FragmentAddedList fragmentAddedList) {
        String ok = user.getId();
        db.child("Quests").child("Аdded-quests").child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {// za ucitvaanje feed questova
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Quest> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(ds.getValue(Quest.class));
                }
                fragmentAddedList.loadAddedQuests(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadFeedQuests(final FeedsListFragment feedsListFragment) {

        db.child("Quests").child("Feed-quests").addListenerForSingleValueEvent(new ValueEventListener() {// za ucitvaanje feed questova
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Quest> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(ds.getValue(Quest.class));
                }
                feedsListFragment.loadFeedQuests(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private static class SingletonHolder {
        public static final LocatorData instance = new LocatorData();

    }

    public static LocatorData getInstance() {
        return SingletonHolder.instance;
    }

    public ArrayList<Quest> getDoneQuests() {
        return doneQuests;
    }

    public ArrayList<Quest> getActiveQuests() {
        return activeQuests;
    }

    public void addActive(Quest quest) {
        activeQuests.add(quest);
    }

    public void addDone(Quest quest) {
        doneQuests.add(quest);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void loadUser(final String Uid, final Activity activity) {
        db.child("Users").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            String ok = Uid;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Intent intent = new Intent(activity, ActivityMain.class);
                activity.startActivity(intent);
                Tools.showMsg(activity, "Login successful.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
