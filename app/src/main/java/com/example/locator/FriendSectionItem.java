package com.example.locator;

public class FriendSectionItem implements FriendsRvItem {
    public boolean found;

    public FriendSectionItem(boolean found) {
        this.found = found;
    }

    @Override
    public int getLayout() {
        return found ? R.layout.item_friend_section_found : R.layout.item_friend_section_friends;
    }

    @Override
    public void bind(FriendAdapter.ViewHolder viewHolder) {

    }
}
