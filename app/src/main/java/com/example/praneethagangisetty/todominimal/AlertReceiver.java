package com.example.praneethagangisetty.todominimal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlertReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "notifications";
    private int notificationID;
    String group = "group_notifications";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        pushNotifications(context, title);
    }

    private void pushNotifications(Context c, String title) {
        createNotificationChannel(c);
        notificationID = new Random().nextInt();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c, CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.notification_alarm);
        mBuilder.setContentTitle(title);
        mBuilder.setGroup(group);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(c);
        notificationManagerCompat.notify(notificationID, mBuilder.build());
    }

    private void createNotificationChannel(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifications";
            String description = "All notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
