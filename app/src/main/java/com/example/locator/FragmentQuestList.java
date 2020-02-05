package com.example.locator;

import android.content.Intent;
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

import static android.app.Activity.RESULT_OK;


public class FragmentQuestList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private QuestAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int type;

    public FragmentQuestList(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        recyclerView = view.findViewById(R.id.recyclerViewFeeds);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        createAdapter();
        updateAdapter();
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        updateAdapter();
        swipeRefreshLayout.setRefreshing(false);
    }


    public void updateAdapter() {
        switch (type) {
            case Constants.QuestType.ACTIVE:
                adapter.setData(LocatorData.getInstance().activeQuests);
                break;
            case Constants.QuestType.FEED:
                adapter.setData(LocatorData.getInstance().feedQuests);
                break;
            case Constants.QuestType.FINISHED:
                adapter.setData(LocatorData.getInstance().finishedQuests);
                break;
        }
    }

    private void createAdapter() {
        switch (type) {
            case Constants.QuestType.ACTIVE:
                adapter = new QuestAdapter(LocatorData.getInstance().activeQuests, getActivity(), type);
                break;
            case Constants.QuestType.FEED:
                adapter = new QuestAdapter(LocatorData.getInstance().feedQuests, getActivity(), type);
                break;
            case Constants.QuestType.FINISHED:
                adapter = new QuestAdapter(LocatorData.getInstance().finishedQuests, getActivity(), type);
                break;
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            updateAdapter();
        }
    }
}
