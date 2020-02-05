package com.example.locator;

public class FriendItem implements FriendRvItem {
    public User user;

    @Override
    public int getLayout() {
        return R.layout.item_friend;
    }

    public FriendItem(User user) {
        this.user = user;
    }

    @Override
    public void bind(FriendAdapter.ViewHolder viewHolder) {

    }
}
