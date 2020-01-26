package com.example.locator;

public class FriendItem implements FriendsRvItem {
    public Friend friend;
    @Override
    public int getLayout() {
        return R.layout.item_friend;
    }
    public FriendItem (Friend friend){
        this.friend = friend;
    }

    @Override
    public void bind(FriendAdapter.ViewHolder viewHolder) {

    }
}
