package com.example.locator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Random;

public class LocatorWorker extends Worker {

    public LocatorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        getApplicationContext().getSystemService(Context.POWER_SERVICE);
    }

    private Location location;

    @NonNull
    @Override
    public Result doWork() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            this.location = location;
            LocatorData.getInstance().updateUserLocation(location.getLongitude(), location.getLatitude());
            LocatorData.getInstance().getUsers(this);
            LocatorData.getInstance().getQuests(this);
        });
        return Result.success();
    }

    public void handleUsersResponse(List<User> users) {
        for (User user : users) {
            Location userLocation = new Location("");
            //userLocation.setLatitude(quest.latitude);
            //userLocation.setLongitude(quest.longitude);
            if (location.distanceTo(userLocation) < 100) {
                generateNotification("User near you", user.getName() + "is near you");
            }
        }
    }

    public void handleQuestsResponse(List<Quest> quests) {
        for (Quest quest : quests) {
            Location questLocation = new Location("");
            //questLocation.setLatitude(quest.getLatitude());
            //questLocation.setLongitude(quest.getLongitude());
            if (location.distanceTo(questLocation) < 100) {
                generateNotification("Quest near you", quest.getName() + "is near you");
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Locator";
            String description = "Locator general notifications.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("general", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void generateNotification(String title, String text) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "general")
            .setSmallIcon(R.drawable.app_icon)
            .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(false);
        Intent contentIntent = new Intent(getApplicationContext(), ActivityMain.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), new Random().nextInt(), contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        createNotificationChannel();
        notificationManager.notify(new Random().nextInt(1000), mBuilder.build());
    }
}
