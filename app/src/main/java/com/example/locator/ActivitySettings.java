package com.example.locator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.concurrent.TimeUnit;

public class ActivitySettings extends ActivityBase {
    private SwitchMaterial switchMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        switchMaterial = findViewById(R.id.switch_material);

        switchMaterial.setChecked(sharedPrefs.getServiceEnabled());

        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Tools.locationPermissionGiven(this)) {
                    startServiceAndWorker();
                    sharedPrefs.putServiceEnabled(true);
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                }
            } else {
                stopServiceAndWorker();
                sharedPrefs.putServiceEnabled(false);
            }
        });
    }


    private void startServiceAndWorker() {
        startService(new Intent(this, LocatorService.class));
        Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

        PeriodicWorkRequest saveRequest =
            new PeriodicWorkRequest.Builder(LocatorWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("locator", ExistingPeriodicWorkPolicy.REPLACE, saveRequest);
    }

    private void stopServiceAndWorker() {
        stopService(new Intent(this, LocatorService.class));
        WorkManager.getInstance(this).cancelUniqueWork("locator");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startServiceAndWorker();
                sharedPrefs.putServiceEnabled(true);
            } else {
                Tools.showMsg(this, "Please allow map permission.");
                switchMaterial.setChecked(false);
                return;
            }
        }
    }

    @Override
    public void initializeComponents() {

    }
}
