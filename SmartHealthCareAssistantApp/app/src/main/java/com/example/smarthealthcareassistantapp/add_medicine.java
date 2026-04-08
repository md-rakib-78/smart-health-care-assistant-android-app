package com.example.smarthealthcareassistantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;

public class add_medicine extends AppCompatActivity {

    EditText StartDate,EndDate;
    ChipGroup chipGroup;
    Chip addTimeChip;
    TextView arrow;
    AutoCompleteTextView freq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medicine);

        arrow = findViewById(R.id.arrow);

        arrow.setOnClickListener(v -> finish());


        // Start Date selection from Calender
        StartDate = findViewById(R.id.startdate);
        StartDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        StartDate.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // End Date selection from Calender
        EndDate = findViewById(R.id.enddate);
        EndDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        EndDate.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Chip group
        chipGroup = findViewById(R.id.chipGroupTime);
        addTimeChip = findViewById(R.id.addTimeChip);
        addTimeChip.setOnClickListener(v -> openTimePicker());

        // Gender dropdown selection
        freq = findViewById(R.id.freq);

        String[] daily = {"1x daily", "2x daily", "3x daily", "4x daily", "5x daily", "6x daily", "7x daily"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,daily);
        freq.setAdapter(adapter);
        freq.setOnClickListener(v -> freq.showDropDown());
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {

                    String time = formatTime(hourOfDay, minute1);
                    addChip(time);

                }, hour, minute, false);

        dialog.show();
    }

    private String formatTime(int hour, int minute) {
        String amPm = (hour >= 12) ? "PM" : "AM";
        hour = (hour == 0) ? 12 : hour % 12;

        return String.format("%02d:%02d %s", hour, minute, amPm);
    }

    private void addChip(String time) {

        Chip chip = new Chip(this);
        chip.setText(time);

        chip.setChipBackgroundColorResource(R.color.light_green);
        chip.setTextColor(getResources().getColor(R.color.green));

        chip.setChipStrokeWidth(1f);
        chip.setChipStrokeColorResource(R.color.green);

        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));

        // Add before "+ Add time"
        int index = chipGroup.indexOfChild(addTimeChip);
        chipGroup.addView(chip, index);
    }
}