package com.example.smarthealthcareassistantapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class AlarmHelper {

    private static final String TAG = "AlarmHelper";

    public static void setAlarm(Context context, int alarmId, String time, String medicineName) {
        try {
            String[] parts = time.replace("AM", "").replace("PM", "").trim().split(":");

            if (parts.length < 2) {
                Log.e(TAG, "Invalid time format: " + time);
                return;
            }

            int hour = Integer.parseInt(parts[0].trim());
            int minute = Integer.parseInt(parts[1].trim());

            boolean isPM = time.contains("PM");
            boolean is12AM = time.contains("AM") && hour == 12;

            if (isPM && hour != 12) hour += 12;
            if (is12AM) hour = 0;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // If the time has already passed today, set it for tomorrow
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medicine_name", medicineName);
            intent.putExtra("alarm_id", alarmId);
            intent.putExtra("time", time);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                Log.d(TAG, "Scheduling alarm for: " + medicineName + " at " + calendar.getTime().toString() + " (ID: " + alarmId + ")");
                
                // Use setExactAndAllowWhileIdle for better reliability on modern Android
                // This requires rescheduling manually in AlarmReceiver, or we can use setRepeating
                // But setRepeating is inexact. Let's use setExactAndAllowWhileIdle for health alerts.
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    } else {
                        // Fallback to inexact if permission not granted
                        alarmManager.setAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                        Log.w(TAG, "Exact alarm permission not granted, using inexact alarm.");
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            pendingIntent
                    );
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting alarm for time: " + time, e);
        }
    }
}
