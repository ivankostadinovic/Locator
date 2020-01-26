package com.example.locator;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Nullable;

public class Friend implements Parcelable {
    public int type;
    public int active_quests;
    public int done_quests;
    public int created_quests;
    public int points;
    public String profile_picture;
    public String name;
    public String id;
    public String location;

    public Friend() {

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Friend) && TextUtils.equals(id, ((Friend) obj).id);
    }

    protected Friend(Parcel in) {
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

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}
