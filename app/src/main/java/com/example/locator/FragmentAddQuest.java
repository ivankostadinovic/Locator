package com.example.locator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class FragmentAddQuest extends Fragment {




    private User user;
    public FragmentAddQuest() {
        // Required empty public constructor
    }

    public static FragmentAddQuest newInstance(User user) {
        FragmentAddQuest fragment = new FragmentAddQuest();
        Bundle args = new Bundle();
        args.putSerializable("User",user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user=(User)getArguments().getSerializable("User");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fragment_add_quest, container, false);
    }







}
