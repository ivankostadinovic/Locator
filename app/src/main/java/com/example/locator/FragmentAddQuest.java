package com.example.locator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.opencv.core.Mat;


public class FragmentAddQuest extends Fragment {




    private User user;
    private Button buttonOut,button;
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

       View view= inflater.inflate(R.layout.fragment_fragment_add_quest, container, false);

         button=(Button) view.findViewById(R.id.btn_page_1);
         buttonOut=(Button) view.findViewById(R.id.btn_page_1_outline);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.INVISIBLE);
                buttonOut.setVisibility(View.VISIBLE);
            }
        });

        buttonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.VISIBLE);
                buttonOut.setVisibility(View.INVISIBLE);
            }
        });

        return  view;
    }







}
