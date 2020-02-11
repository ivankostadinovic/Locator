package com.example.locator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentFriends extends Fragment {
    private FriendAdapter adapter;
    private RecyclerView recyclerView;


    public FragmentFriends() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new FriendAdapter(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        LocatorData.getInstance().setFriendListener(new FriendsListener() {
            @Override
            public void friendsLoaded(User user) {
                adapter.addData(user);
            }

            @Override
            public void updateFriend(User user) {
                adapter.updateData(user);
            }
        });
        adapter.setData(LocatorData.getInstance().friends);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocatorData.getInstance().loadFriends();
    }
}
