package com.example.locator;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class LocatorData {

    public List<Quest> feedQuests;
    public List<Quest> activeQuests;
    public List<Quest> finishedQuests;
    public List<Quest> addedQuests;
    public List<QuestItem> itemsToAdd;
    private User user;
    private DatabaseReference db;
    private FirebaseAuth auth;


    private LocatorData() {
        feedQuests = new ArrayList<>();
        addedQuests = new ArrayList<>();
        finishedQuests = new ArrayList<>();
        activeQuests = new ArrayList<>();
        user = new User();
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

    }

    public void feedQuestListener(final FeedsListFragment feedsListFragment) {
        db.child("Quests").child("Feed-quests").addChildEventListener(new ChildEventListener() {// za ucitvaanje feed questova
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Quest quest = dataSnapshot.getValue(Quest.class);
                boolean exists = false;
                for (Quest activeQuest : activeQuests) {
                    if (activeQuest.getId().equals(quest.getId())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    feedQuests.add(quest);
                    feedsListFragment.addQuest(quest);
                }

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

    public void addedQuestListener(final FragmentAddedList fragmentAddedList) {
        db.child("Quests").child("Added-quests").child(user.getId()).addChildEventListener(new ChildEventListener() {// za ucitvaanje feed questova
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Quest quest = dataSnapshot.getValue(Quest.class);
                addedQuests.add(quest);
                fragmentAddedList.addQuest(quest);
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

    public void activeQuestListener(final FragmentActiveList fragmentActiveList) {
        db.child("Quests").child("Active-quests").child(getUser().getId()).addChildEventListener(new ChildEventListener() {// za ucitvaanje feed questova
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Quest quest = dataSnapshot.getValue(Quest.class);
                activeQuests.add(quest);
                fragmentActiveList.addQuest(quest);
                Tools.log("heeloo");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Quest quest = dataSnapshot.getValue(Quest.class);
                for (int i = 0; i < activeQuests.size(); i++) {
                    if (activeQuests.get(i).getId().equals(quest.getId())) {
                        activeQuests.remove(i);
                    }
                }
                fragmentActiveList.removeQuest(quest);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void finishQuestListener(final FragmentFinishedList fragmentFinishedList) {
        db.child("Quests").child("Finished-quests").child(getUser().getId()).addChildEventListener(new ChildEventListener() {// za ucitvaanje feed questova
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Quest quest = dataSnapshot.getValue(Quest.class);
                finishedQuests.add(quest);
                fragmentFinishedList.addQuest(quest);
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


    public void takeQuest(Quest quest) {
        db.child("Quests").child("Active-quests").child(getUser().getId()).child(quest.getId()).setValue(quest);
        // Tools.showMsg(activity, "Quest added to active");
    }

    public void addQuest(Quest quest, Activity activity) {

        String key = db.child("Feed-quests").push().getKey();
        quest.setId(key);
        db.child("Quests").child("Feed-quests").child(key).setValue(quest);
        db.child("Quests").child("Added-quests").child(getUser().getId()).child(key).setValue(quest);
        Tools.showMsg(activity, "Quest added");
    }

    public void updateQuestProgress(Quest quest) {
        for (int i = 0; i < activeQuests.size(); i++) {
            if (activeQuests.get(i).getId().equals(quest.getId())) {
                activeQuests.set(i, quest);
            }
        }
        db.child("Quests").child("Active-quests").child(getUser().getId()).child(quest.getId()).setValue(quest);
    }

    public void finishQuest(Quest quest) {

        db.child("Quests").child("Finished-quests").child(getUser().getId()).child(quest.getId()).setValue(quest);
        db.child("Quests").child("Active-quests").child(getUser().getId()).child(quest.getId()).removeValue();
    }


    public void addFriend(FriendModel friend) {
    }


    private static class SingletonHolder {
        public static final LocatorData instance = new LocatorData();

    }

    public static LocatorData getInstance() {
        return SingletonHolder.instance;
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

    public void registerUser(final User user, Activity activity) {

        final OnCompleteListener<AuthResult> register = task -> {
            if (task.isSuccessful()) {
                FirebaseUser u = task.getResult().getUser();
                db.child("Users").child(u.getUid()).setValue(user);
                db.child("Users").child(u.getUid()).child("id").setValue(u.getUid());
                Tools.showMsg(activity, "Registration complete.");
            } else {
                Tools.showMsg(activity, "User with email already exists.");
            }
        };
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(activity, register);
    }

}
