package com.example.locator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {

    public static String LOCATION_SERVICE_ENABLED = "location_service_enabled";
    public static final String LOCATION_DIALOG_SHOWN = "location_dialog_shown";

    @Retention(RetentionPolicy.SOURCE)
    public @interface QuestType {
        int FEED = 0;
        int ACTIVE = 1;
        int FINISHED = 2;
        int ADDED = 3;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FriendType {
        int ADDED = 0;
        int FOUND = 1;
    }

}
