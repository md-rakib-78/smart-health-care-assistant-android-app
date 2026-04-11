package com.example.smarthealthcareassistantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import database.*;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;

import database.AppDatabase;
import database.Medicine;

public class add_medicine extends AppCompatActivity {

    EditText StartDate,EndDate,medname;
    ChipGroup chipGroup;
    Chip addTimeChip;
    TextView arrow;
    AutoCompleteTextView freq;
    Button save;
    public String getTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medicine);

        arrow = findViewById(R.id.arrow);
        save = findViewById(R.id.saveB);
        medname = findViewById(R.id.medname);


        //Insert value into room database
        save.setOnClickListener(v->{

            String PN = medname.getText().toString();
            String SD = StartDate.getText().toString();
            String ED = EndDate.getText().toString();
            String F = freq.getText().toString();
            String T = getTime;

            int year,year2,month,month2;



            //text field condition
            if (PN.isEmpty()) {
                medname.setError("Medicine name is required");
                medname.requestFocus();
                return;
            }

            if(F.isEmpty()) {
                freq.setError("Frequency is required");
                freq.requestFocus();
                return;
            }

            if(SD.isEmpty()) {
                Toast.makeText(this, "Start date is required", Toast.LENGTH_SHORT).show();
                StartDate.setError("");
                StartDate.requestFocus();
                return;
            }else
            {
                String[] date = SD.split("/");
                month = Integer.parseInt(date[1]);
                year = Integer.parseInt(date[2]);

            }

            if(ED.isEmpty()) {
                Toast.makeText(this, "End date is required", Toast.LENGTH_SHORT).show();
                EndDate.setError("");
                EndDate.requestFocus();
                return;
            }else
            {
                String[] date2 = ED.split("/");
                month2 = Integer.parseInt(date2[1]);
                year2 = Integer.parseInt(date2[2]);
            }

            if(year > year2) {
                Toast.makeText(this, "End year should be greater than start year", Toast.LENGTH_SHORT).show();
                EndDate.setError("");
                EndDate.requestFocus();
                return;
            }

            if(year == year2 && month > month2)
            {
                Toast.makeText(this, "End month should be greater than start month", Toast.LENGTH_SHORT).show();
                EndDate.setError("");
                EndDate.requestFocus();
                return;
            }


            if(chipGroup.getChildCount() <= 1) {
                addTimeChip.setError("");
                Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show();
                return;
            }



            // Room database insert value
            AppDatabase db = AppDatabase.getDatabase(this);

            String Mname = medname.getText().toString();
            String start = StartDate.getText().toString();
            String end = EndDate.getText().toString();
            String frequency = freq.getText().toString();
            String times = getTime; // your chip method

            Medicine medicine = new Medicine();
            medicine.MedicineName = Mname;
            medicine.startDate = start;
            medicine.endDate = end;
            medicine.frequency = frequency;
            medicine.times = times;

            db.medicineDao().insert(medicine);

            Toast.makeText(this, "Medicine Added", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(add_medicine.this, home_dashboard_activity.class));
            finish();
        });




        // Back Button
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

        getTime = time;
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