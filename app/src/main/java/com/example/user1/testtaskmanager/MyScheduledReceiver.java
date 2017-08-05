package com.example.user1.testtaskmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class MyScheduledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent scheduledIntent = new Intent(context, MainActivity.class);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                scheduledIntent, 0);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        ArrayList<String> arrayList = intent.getStringArrayListExtra(MainActivity.INTENT_LIST_FOR_NOTIFICATION);
        for (int i = 0; i < arrayList.size(); i++) {
            Notification notification = new Notification.Builder(context)
                    .setContentIntent(contentIntent)
                    .setContentTitle(arrayList.get(i))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("")
                    .setWhen(System.currentTimeMillis()).setAutoCancel(true)
                    .build();
            notificationManager.notify(i, notification);
        }

    }
}
