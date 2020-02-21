package com.example.mixit.services.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mixit.R;
import com.example.mixit.models.Item;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Item item = (Item) intent.getSerializableExtra("item");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                "notificationChannel").setSmallIcon(R.drawable.logo)
                .setContentTitle("Time to Mix It!")
//                .setContentText("Let's make your new favourite cocktail "+item.getTitle()+"!")
                .setContentText("Let's make your new favourite cocktail!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
