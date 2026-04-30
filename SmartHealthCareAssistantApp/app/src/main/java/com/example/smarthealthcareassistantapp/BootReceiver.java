package com.example.smarthealthcareassistantapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import database.AppDatabase;
import database.MedicineTime;
import database.MedicineWithTimes;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            //  reload all medicines from DB
            AppDatabase db = AppDatabase.getDatabase(context);

            List<MedicineWithTimes> list =
                    db.medicineDao().getMedicinesWithTimes();

            for (MedicineWithTimes m : list) {

                for (MedicineTime t : m.times) {

                    AlarmHelper.setAlarm(
                            context,
                            t.alarmId,
                            t.time,
                            m.medicine.MedicineName
                    );
                }
            }
        }
    }
}