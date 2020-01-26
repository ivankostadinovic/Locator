package com.example.locator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<FriendsRvItem> list = new ArrayList<>();
    private Context context;
    private int type_section_friends = 0, type_friend = 1, type_section_found = 2;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == type_friend) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        } else if (viewType == type_section_friends) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_section_friends, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_section_found, parent, false);

        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) instanceof FriendItem ? type_friend : ((FriendSectionItem) list.get(position)).found ? type_section_found : type_section_friends;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendsRvItem item = list.get(position);
        if (item instanceof FriendItem) {
            holder.name.setText(((FriendItem) item).friend.name);
            holder.location.setText(((FriendItem) item).friend.location);
            holder.image.setImageBitmap(Tools.StringToBitMap(((FriendItem) item).friend.profile_picture));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public FriendAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Friend> list) {
        this.list.add(new FriendSectionItem(false));
        this.list.add(new FriendSectionItem(true));
        for (Friend friend : list) {
            if (friend.type == Constants.FriendType.FOUND) {
                this.list.add(this.list.size(), new FriendItem(friend));
            } else {
                this.list.add(1, new FriendItem(friend));
            }
        }
        notifyDataSetChanged();
    }

    public void moveDiscoveredToAdded(String id) {
        for (FriendsRvItem friendsRvItem : list) {
            if (friendsRvItem instanceof FriendItem && ((FriendItem) friendsRvItem).friend.id.equals(id)) {
                Friend friend = ((FriendItem) friendsRvItem).friend;
                friend.type = Constants.FriendType.ADDED;
                list.remove(friendsRvItem);
                list.add(1, new FriendItem(friend));
                notifyItemInserted(1);

            }
        }
    }

    public void addDiscovered(Friend friend) {
        this.list.add(new FriendItem(friend));
        notifyItemInserted(list.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, location;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            image = itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (list.get(position) instanceof FriendItem) {
                Friend friend = ((FriendItem) list.get(position)).friend;
                switch (friend.type) {
                    case Constants.FriendType.ADDED:
                        Intent i = new Intent(context, PopUpFriendActivity.class);
                        i.putExtra("friend", friend);
                        context.startActivity(i);
                        break;
                    case Constants.FriendType.FOUND:
                        new MaterialAlertDialogBuilder(context)
                            .setTitle("Add friend")
                            .setMessage("Do you wish to add " + friend.name + " to your friends list?")
                            .setPositiveButton("Ok", (dialog, which) -> {
                                LocatorData.getInstance().addFriend(friend);
                                dialog.dismiss();
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .show();
                        break;
                }
            }
        }
    }
}
