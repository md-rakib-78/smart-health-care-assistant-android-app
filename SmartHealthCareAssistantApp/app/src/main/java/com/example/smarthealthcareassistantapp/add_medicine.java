package com.example.smarthealthcareassistantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import database.*;

public class add_medicine extends AppCompatActivity {

    EditText StartDate, EndDate, medname;
    ChipGroup chipGroup;
    Chip addTimeChip;
    AutoCompleteTextView freq;
    Button save;
    TextView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        // init views
        medname = findViewById(R.id.medname);
        StartDate = findViewById(R.id.startdate);
        EndDate = findViewById(R.id.enddate);
        chipGroup = findViewById(R.id.chipGroupTime);
        addTimeChip = findViewById(R.id.addTimeChip);
        freq = findViewById(R.id.freq);
        save = findViewById(R.id.saveB);
        arrow = findViewById(R.id.arrow);

        // frequency dropdown
        String[] daily = {"1x daily", "2x daily", "3x daily", "4x daily", "5x daily"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, daily);
        freq.setAdapter(adapter);
        
        // Show dropdown on click
        freq.setOnClickListener(v -> freq.showDropDown());

        // add time chip
        addTimeChip.setOnClickListener(v -> openTimePicker());

        // date pickers
        StartDate.setOnClickListener(v -> showDatePicker(StartDate));
        EndDate.setOnClickListener(v -> showDatePicker(EndDate));

        // back button
        arrow.setOnClickListener(v -> finish());
        
        // SAVE BUTTON
        save.setOnClickListener(v -> saveMedicine());
    }

    // ================= SAVE MEDICINE =================
    private void saveMedicine() {

        String name = medname.getText().toString().trim();
        String start = StartDate.getText().toString().trim();
        String end = EndDate.getText().toString().trim();
        String frequency = freq.getText().toString().trim();

        // validation
        if (name.isEmpty()) {
            medname.setError("Required");
            return;
        }

        if (start.isEmpty() || end.isEmpty()) {
            Toast.makeText(this, "Select dates", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> times = getAllTimesFromChips();

        if (times.isEmpty()) {
            Toast.makeText(this, "Select at least one time", Toast.LENGTH_SHORT).show();
            return;
        }

        save.setEnabled(false);

        // DB - Run in background thread
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);

            // 1. insert medicine
            Medicine medicine = new Medicine();
            medicine.MedicineName = name;
            medicine.startDate = start;
            medicine.endDate = end;
            medicine.frequency = frequency;

            long medicineId = db.medicineDao().insert(medicine);

            // 2. insert times + alarms
            List<MedicineTime> timeList = new ArrayList<>();

            for (int i = 0; i < times.size(); i++) {

                String time = times.get(i);

                // Use a larger multiplier to avoid alarm ID collisions between different medicines
                int alarmId = (int) (medicineId * 100) + i;

                MedicineTime mt = new MedicineTime((int) medicineId, time, alarmId);
                timeList.add(mt);

                // set alarm
                AlarmHelper.setAlarm(this, alarmId, time, name);
            }

            db.medicineDao().insertTimes(timeList);

            runOnUiThread(() -> {
                Toast.makeText(this, "Medicine Saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, home_dashboard_activity.class));
                finish();
            });
        }).start();
    }

    // ================= GET ALL CHIP TIMES =================
    private List<String> getAllTimesFromChips() {

        List<String> list = new ArrayList<>();

        for (int i = 0; i < chipGroup.getChildCount(); i++) {

            View v = chipGroup.getChildAt(i);

            if (v instanceof Chip) {
                Chip chip = (Chip) v;
                // Avoid adding the static "Add Time" chip text to the medicine times
                if (chip != addTimeChip) {
                    list.add(chip.getText().toString());
                }
            }
        }

        return list;
    }

    // ================= TIME PICKER =================
    private void openTimePicker() {

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog dialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {

                    String time = format12Hour(hourOfDay, minute);
                    addChip(time);

                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // IMPORTANT: false = 12-hour mode
        );

        dialog.show();
    }

    private String format12Hour(int hour, int minute) {

        String amPm = (hour >= 12) ? "PM" : "AM";

        int hour12 = hour % 12;
        if (hour12 == 0) hour12 = 12;

        return String.format("%02d:%02d %s", hour12, minute, amPm);
    }

    // ================= ADD CHIP =================
    private void addChip(String time) {

        Chip chip = new Chip(this);
        chip.setText(time);
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));

        chipGroup.addView(chip);
    }

    // ================= DATE PICKER =================
    private void showDatePicker(EditText editText) {

        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    String date = day + "/" + (month + 1) + "/" + year;
                    editText.setText(date);
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }
}
