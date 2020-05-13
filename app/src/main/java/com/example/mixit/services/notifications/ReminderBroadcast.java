package com.example.mixit.services.notifications;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mixit.R;
import com.example.mixit.activities.MainActivity;
import com.example.mixit.models.Item;
import com.example.mixit.preferences.SessionPreferences;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String itemId = (String) intent.getSerializableExtra("id");
        SessionPreferences mSessionPreferences = new SessionPreferences(context, null, null);
        Item item = mSessionPreferences.deleteReminder(SessionPreferences.PREF_REMINDERS, itemId);

        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntent.putExtra("id", itemId);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                "notificationChannel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Time to Mix It!")
                .setContentText("Let's prepare "+item.getTitle()+", your new favourite cocktail!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notifyPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
