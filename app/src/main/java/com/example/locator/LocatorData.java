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

public class LocatorData {

    private ArrayList<Quest> doneQuests;
    private ArrayList<Quest> activeQuests;
    private User user;
    private DatabaseReference db;


    private LocatorData()
    {
        doneQuests=new ArrayList<>();
        activeQuests=new ArrayList<>();
        user =new User();
        db= FirebaseDatabase.getInstance().getReference();

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

    public void loadUser(String Uid, final Activity activity)
    {
        db.child("Users").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
