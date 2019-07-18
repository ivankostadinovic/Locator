package com.example.locator;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.Locator;

import java.util.ArrayList;
import java.util.List;

public class LocatorData {

    private  ArrayList<Quest> doneQuests;
    private  ArrayList<Quest> activeQuests;
    private  User user;
    private   DatabaseReference db;


    private LocatorData()
    {
        doneQuests=new ArrayList<>();
        activeQuests=new ArrayList<>();
        user =new User();
        db= FirebaseDatabase.getInstance().getReference();

    }
    public void loadAddedQuests(final FragmentAddedList fragmentAddedList)
    {
        String ok=user.getId();
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

    public  void loadFeedQuests(final FeedsListFragment feedsListFragment) {

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
    public void setUser(User user)
    {
        this.user=user;
    }
    public User getUser(){ return user;}

    public void loadUser(final String Uid, final Activity activity)
    {
        db.child("Users").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            String ok=Uid;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                Intent intent=new Intent(activity,ActivityMain.class);
                activity.startActivity(intent);
                Toast.makeText(activity, "Login successful.", Toast.LENGTH_LONG).show();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
