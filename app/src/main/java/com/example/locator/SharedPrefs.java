package com.example.locator;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    private static SharedPrefs sInstance;
    private SharedPreferences mSharedPrefs;

    private SharedPrefs(Context context) {
        mSharedPrefs = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        mSharedPrefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {

        });
    }

    public static SharedPrefs getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPrefs(context);
        }
        return sInstance;
    }

    public boolean getServiceEnabled() {
        return mSharedPrefs.getBoolean(Constants.LOCATION_SERVICE_ENABLED, true);
    }

    public void putServiceEnabled(boolean serviceEnabled) {
        mSharedPrefs.edit().putBoolean(Constants.LOCATION_SERVICE_ENABLED, serviceEnabled).apply();
    }

    public boolean getServiceDialogShown() {
        return mSharedPrefs.getBoolean(Constants.LOCATION_DIALOG_SHOWN, false);
    }

    public void saveServiceDialogShown(boolean serviceDialogShown) {
        mSharedPrefs.edit().putBoolean(Constants.LOCATION_DIALOG_SHOWN, serviceDialogShown).apply();
    }

    public void clear() {
        mSharedPrefs.edit().clear().apply();
    }
}
