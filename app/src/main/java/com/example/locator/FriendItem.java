package com.example.locator;

public class FriendItem implements FriendRvItem {
    public FriendModel friend;
    @Override
    public int getLayout() {
        return R.layout.item_friend;
    }
    public FriendItem (FriendModel friend){
        this.friend = friend;
    }

    @Override
    public void bind(FriendAdapter.ViewHolder viewHolder) {

    }
}
