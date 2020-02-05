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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class LocatorData {

    public List<Quest> feedQuests;
    public List<Quest> activeQuests;
    public List<Quest> finishedQuests;
    public List<QuestItem> itemsToAdd;
    public List<User> friends;
    private User user;
    private DatabaseReference db;
    private FirebaseAuth auth;
    private UserActionListener listener;
    private FriendsListener friendsListener;


    private LocatorData() {
        feedQuests = new ArrayList<>();
        finishedQuests = new ArrayList<>();
        activeQuests = new ArrayList<>();
        friends = new ArrayList<>();
        user = new User();
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

    }

    public void setFriendListener(FriendsListener listener) {
        this.friendsListener = listener;
    }

    public void setListener(UserActionListener listener) {
        this.listener = listener;
    }

    public void userLocationListener() {
        db.child("Users").child(getUser().getId()).child("latitude").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Double lat = dataSnapshot.getValue(Double.class);
                user.setLatitude(lat);
                if (listener != null) {
                    listener.userLatitudeChagned(lat);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db.child("Users").child(getUser().getId()).child("longitude").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Double lon = dataSnapshot.getValue(Double.class);
                user.setLongitude(lon);
                if (listener != null) {

                    listener.userLongitudeChanged(lon);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void feedQuestListener() {

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
                for (Quest finishedQuest : finishedQuests) {
                    if (finishedQuest.getId().equals(quest.getId())) {
                        exists = true;
                    }
                }
                if (quest.getCreatedBy().equals(getUser().getId())) {
                    exists = true;
                }
                if (!exists) {
                    feedQuests.add(quest);
                    if (listener != null)
                        listener.newFeedQuest(quest);
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

    public void observeFriends() {
        db.child("Friends").child(user.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User friend = dataSnapshot.getValue(User.class);
                boolean exists = false;
                for (User friend1 : friends) {
                    if (friend1.getId().equals(friend.getId())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    friendsListener.friendsLoaded(friend);
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadFriends() {
        Query query = db.child("Friends").child(user.getId()).orderByChild("points");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    User friend = childDataSnapshot.getValue(User.class);
                    friends.add(friend);
                    friendsListener.friendsLoaded(friend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void observeFriendLocations(FragmentMap fragmentMap) {
        for (User friend : friends) {
            db.child("Friends").child(user.getId()).child(friend.getId()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (fragmentMap != null) {
                        if ("longitude".equals(dataSnapshot.getKey())) {
                            friend.setLongitude(dataSnapshot.getValue(Double.class));
                        } else {
                            friend.setLatitude(dataSnapshot.getValue(Double.class));
                        }
                        fragmentMap.updateAddFriend(friend);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void activeQuestListener() {
        if (getUser().getId() != null)
            db.child("Quests").child("Active-quests").child(getUser().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Quest quest = child.getValue(Quest.class);
                        activeQuests.add(quest);
                    }
                    finishedQuestListener();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }


    public void finishedQuestListener() {
        db.child("Quests").child("Finished-quests").child(getUser().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Quest quest = child.getValue(Quest.class);
                    finishedQuests.add(quest);
                }
                feedQuestListener();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void takeQuest(Quest quest) {
        db.child("Quests").child("Active-quests").child(getUser().getId()).child(quest.getId()).setValue(quest);
        for (int i = 0; i < feedQuests.size(); i++) {
            if (quest.getId().equals(feedQuests.get(i).getId())) {
                feedQuests.remove(i);
                break;
            }
        }
        activeQuests.add(quest);
        // Tools.showMsg(activity, "Quest added to active");
    }

    public void addQuest(Quest quest, Activity activity) {

        String key = db.child("Feed-quests").push().getKey();
        quest.setId(key);
        quest.setCreatedBy(getUser().getId());
        db.child("Quests").child("Feed-quests").child(key).setValue(quest);
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
        for (int i = 0; i < activeQuests.size(); i++) {
            if (quest.getId().equals(activeQuests.get(i).getId())) {
                activeQuests.remove(i);
                break;
            }
        }
        finishedQuests.add(quest);
    }

    public void updateUserLocation(double longitude, double latitude) {
        db.child("Users").child(getUser().getId()).child("longitude").setValue(longitude);
        db.child("Users").child(getUser().getId()).child("latitude").setValue(latitude);
        for (User friend : friends) {
            db.child("Friends").child(friend.getId()).child(user.getId()).child("longitude").setValue(longitude);
            db.child("Friends").child(friend.getId()).child(user.getId()).child("latitude").setValue(latitude);
        }
    }


    public void addFriend(User friend) {
        db.child("Friends").child(friend.getId()).child(user.getId()).setValue(getUser());
        db.child("Friends").child(getUser().getId()).child(friend.getId()).setValue(friend);
    }

    public void addFriend(String friendId) {
        getFriend(friendId);
    }

    public void getFriend(String id) {
        db.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                addFriend(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getUsers(LocatorWorker worker) {
        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    User user = childDataSnapshot.getValue(User.class);
                    users.add(user);
                }
                worker.handleUsersResponse(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getQuests(LocatorWorker worker) {
        db.child("Quests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Quest> quests = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Quest quest = childDataSnapshot.getValue(Quest.class);
                    quests.add(quest);
                }
                worker.handleQuestsResponse(quests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateUserPoints(int points) {
        db.child("Users").child(getUser().getId()).child("points").setValue(user.getPoints() + points);
        for (User friend : friends) {
            db.child("Users").child(friend.getId()).child(user.getId()).child("points").setValue(user.getPoints() + points);
        }
    }

    public User getFriendLocal(String friend_id) {
        for (User friend : friends) {
            if (friend.getId().equals(friend_id)) {
                return friend;
            }
        }
        return null;
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

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Intent intent = new Intent(activity, ActivityMain.class);
                activity.startActivity(intent);
                activity.finish();
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
