package com.example.locator;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Nullable;

public class FriendModel implements Parcelable {
    public int type;
    public int active_quests;
    public int done_quests;
    public int created_quests;
    public int points;
    public String profile_picture;
    public String name;
    public String id;
    public String location;

    public FriendModel() {

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof FriendModel) && TextUtils.equals(id, ((FriendModel) obj).id);
    }

    protected FriendModel(Parcel in) {
        type = in.readInt();
        active_quests = in.readInt();
        done_quests = in.readInt();
        created_quests = in.readInt();
        points = in.readInt();
        profile_picture = in.readString();
        name = in.readString();
        id = in.readString();
        location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(active_quests);
        dest.writeInt(done_quests);
        dest.writeInt(created_quests);
        dest.writeInt(points);
        dest.writeString(profile_picture);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FriendModel> CREATOR = new Creator<FriendModel>() {
        @Override
        public FriendModel createFromParcel(Parcel in) {
            return new FriendModel(in);
        }

        @Override
        public FriendModel[] newArray(int size) {
            return new FriendModel[size];
        }
    };
}
