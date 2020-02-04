package com.example.locator;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocatorService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;


    public LocatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Tools.showMsg(this, "Service started");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setSmallestDisplacement(5);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Tools.showMsg(LocatorService.this, "Location received");
                LocatorData.getInstance().updateUserLocation(locationResult.getLastLocation().getLongitude(), locationResult.getLastLocation().getLatitude());
            }
        };
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Tools.showMsg(this, "Service killed");
        fusedLocationClient.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }
}
