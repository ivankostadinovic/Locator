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

    private ArrayList<Quest> doneQuests;
    private ArrayList<Quest> activeQuests;
    public List<QuestItem> itemsToAdd;
    private User user;
    private DatabaseReference db;
    private FirebaseAuth auth;


    private LocatorData() {
        doneQuests = new ArrayList<>();
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
                feedsListFragment.addQuest(quest);

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
        db.child("Quests").child("Added-quests").child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {// za ucitvaanje feed questova
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
                Tools.log("heeloo" + " why");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void takeQuest(Quest quest, Activity activity) {
        db.child("Quests").child("Active-quests").child(getUser().getId()).child(quest.getId()).setValue(quest);
        Tools.showMsg(activity, "Quest added to active");
    }

    public void addQuest(Quest quest, Activity activity) {

        String key = db.child("Feed-quests").push().getKey();
        quest.setId(key);
        db.child("Quests").child("Feed-quests").child(key).setValue(quest);
        db.child("Quests").child("Added-quests").child(getUser().getId()).child(key).setValue(quest);
        Tools.showMsg(activity, "Quest added");
    }

    public void updateQuestProgress(Quest quest) {
        db.child("Quests").child("Active-quests").child(getUser().getId()).child(quest.getId()).setValue(quest);
    }

    public void finishQuest(Quest quest) {

        db.child("Quests").child("Finished-quests").child(getUser().getId()).child(quest.getId()).setValue(quest);
        db.child("Quests").child("Active-quests").child(getUser().getId()).child(quest.getId()).removeValue();
    }

    public void loadActiveQuests(FragmentActiveList fragmentActiveList) {

        db.child("Quests").child("Active-quests").child(getUser().getId()).addListenerForSingleValueEvent(new ValueEventListener() {// za ucitvaanje active questova
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Quest> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(ds.getValue(Quest.class));
                }
                fragmentActiveList.loadActiveQuests(list);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void loadFinishedQuests(FragmentFinishedList fragmentFinishedList) {

        db.child("Quests").child("Finished-quests").child(getUser().getId()).addListenerForSingleValueEvent(new ValueEventListener() {// za ucitvaanje active questova
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Quest> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(ds.getValue(Quest.class));
                }
                fragmentFinishedList.loadFinishedQuests(list);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addFriend(FriendModel friend) {
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
