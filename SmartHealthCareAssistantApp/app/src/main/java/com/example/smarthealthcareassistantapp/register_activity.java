package com.example.smarthealthcareassistantapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class register_activity extends AppCompatActivity {

    EditText dateOfBirth;
    AutoCompleteTextView gender,blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Date of Birth selection from calender
        dateOfBirth = findViewById(R.id.dateOfBirth);

        dateOfBirth.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        dateOfBirth.setText(date);

                    },
                    year, month, day
            );

            datePickerDialog.show();
        });


        // Gender dropdown selection
        gender = findViewById(R.id.gender);

        String[] genderList = {"Male", "Female", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                genderList
        );
        gender.setAdapter(adapter);
        gender.setOnClickListener(v -> gender.showDropDown());


        // Blood Group dropdown selection
        blood = findViewById(R.id.blood);

        String[] bloodList = {   "A+","A-","B+","B-","O+","O-","AB+","AB-"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                bloodList
        );
        blood.setAdapter(adapter1);
        blood.setOnClickListener(v -> blood.showDropDown());


    }
}