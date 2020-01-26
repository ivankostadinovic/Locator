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

import java.util.ArrayList;
import java.util.List;

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

        List<FriendModel> lista = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            FriendModel friend = new FriendModel();
            friend.id = String.valueOf(i);
            friend.name = "Pera" + i;
            friend.location = "Belgrade, Serbia";
            friend.type = i % 2 == 0 ? Constants.FriendType.ADDED : Constants.FriendType.FOUND;
            lista.add(friend);
        }
        adapter.setData(lista);
        FriendModel friend = new FriendModel();
        friend.id = "7";
        friend.name = "Djoka";
        friend.location = "Nis, Serbia";
        friend.type = Constants.FriendType.FOUND;
        adapter.addDiscovered(friend);
        adapter.moveDiscoveredToAdded("7");
    }
}
