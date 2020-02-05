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

public class FeedsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;
    private QuestAdapter adapter;
    private boolean initialState = true;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static FragmentQuests newInstance(User user) {
        FragmentQuests fragment = new FragmentQuests();
        Bundle args = new Bundle();
        args.putSerializable("User", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds_list, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);

        recyclerView = view.findViewById(R.id.recyclerViewFeeds);

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

        if (adapter == null) {
            adapter = new QuestAdapter(LocatorData.getInstance().feedQuests, getActivity(), Constants.QuestType.FEED);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(LocatorData.getInstance().feedQuests);
        }

    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setRefreshing(false);
        if (adapter == null) {
            adapter = new QuestAdapter(LocatorData.getInstance().feedQuests, getActivity(), Constants.QuestType.FEED);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(LocatorData.getInstance().feedQuests);
        }

    }


    public void updateAdapter() {
        if (adapter == null) {
            adapter = new QuestAdapter(LocatorData.getInstance().feedQuests, getActivity(), Constants.QuestType.FEED);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(LocatorData.getInstance().feedQuests);
        }
    }
}
