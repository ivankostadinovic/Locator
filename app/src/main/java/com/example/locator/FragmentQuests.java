package com.example.locator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class FragmentQuests extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Quest> questList;




    private User user;
    public FragmentQuests() {
        // Required empty public constructor
    }

    public static FragmentQuests newInstance(User user) {
        FragmentQuests fragment = new FragmentQuests();
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

        View view=inflater.inflate(R.layout.fragment_fragment_quests, container, false);
        //recyclerView=view.findViewById(R.id.recyclerView);
        //adapter=new QuestAdapter(questList,getActivity());
        //recyclerView.setAdapter(adapter);
        return  view;
    }







}
