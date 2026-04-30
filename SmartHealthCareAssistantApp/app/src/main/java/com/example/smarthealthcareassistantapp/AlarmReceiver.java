package com.example.smarthealthcareassistantapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String medicineName = intent.getStringExtra("medicine_name");
        int alarmId = intent.getIntExtra("alarm_id", -1);
        String time = intent.getStringExtra("time");

        Log.d("AlarmReceiver", "Triggered: " + medicineName);

        String channelId = "medicine_channel";

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // ================= CHANNEL =================
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Medicine Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );

            manager.createNotificationChannel(channel);
        }

        // ================= OPEN APP =================
        Intent openApp = new Intent(context, home_dashboard_activity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                alarmId,
                openApp,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // ================= LARGE ICON SAFETY =================
        Bitmap largeIcon = null;

        try {
            largeIcon = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.health_logo_one
            );
        } catch (Exception e) {
            Log.e("AlarmReceiver", "Large icon error", e);
        }

        // ================= BUILD NOTIFICATION =================
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_notification) // MUST EXIST
                        .setContentTitle("💊 Medicine Reminder")
                        .setContentText("Take: " + (medicineName != null ? medicineName : "Medicine"))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon);
        }

        // ================= SHOW NOTIFICATION =================
        manager.notify(
                alarmId != -1 ? alarmId : (int) System.currentTimeMillis(),
                builder.build()
        );

        // ================= RESCHEDULE =================
        if (time != null && alarmId != -1 && medicineName != null) {
            AlarmHelper.setAlarm(context, alarmId, time, medicineName);
        }
    }
}