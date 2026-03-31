package com.example.smarthealthcareassistantapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class edit_profile_activity extends AppCompatActivity {

    EditText dateOfBirth;
    AutoCompleteTextView gender,blood;
    ImageView profilePic;
    LinearLayout profileButton;

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    profilePic.setImageURI(imageUri);
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        //Profile image upload
        profilePic = findViewById(R.id.profilePic);
        profileButton = findViewById(R.id.logo);

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });



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