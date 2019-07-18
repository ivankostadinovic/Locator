package com.example.locator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentAddedList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;
    private QuestAdapter adapter;
    private List<Quest> questList;
    private  SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_added_list,container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout =  view.findViewById(R.id.swipe_container);
        recyclerView=view.findViewById(R.id.recyclerViewAdded);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        if(questList==null)
            LocatorData.getInstance().loadAddedQuests(this);
        else
            loadAddedQuests(questList);
        adapter=new QuestAdapter(questList,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setRefreshing(false);

    }

    public void loadAddedQuests(List<Quest> list) {
        questList=list;
        adapter=new QuestAdapter(questList,getActivity());
        recyclerView.setAdapter(adapter);
    }
}
