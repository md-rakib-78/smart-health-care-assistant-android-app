package com.example.smarthealthcareassistantapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import database.AppDatabase;
import database.User;

public class edit_profile_activity extends AppCompatActivity {

    EditText dateOfBirth,name,weight,height;
    AutoCompleteTextView gender,blood;
    ImageView profilePic;
    LinearLayout profileButton;
    Button save;
    String imgPath;

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();

                    try {
                        String fileName = "profile_image.jpg";
                        Uri savedUri = saveImageToInternalStorage(imageUri, fileName);

                        //GET FILE PATH HERE
                        File file = new File(getFilesDir(), fileName);
                        imgPath = file.getAbsolutePath();

                        // Show image in ImageView
                        profilePic.setImageURI(savedUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );


    // Save Image in Internally
    private Uri saveImageToInternalStorage(Uri sourceUri, String fileName) throws IOException {

        InputStream inputStream = getContentResolver().openInputStream(sourceUri);

        File file = new File(getFilesDir(), fileName); // internal storage
        OutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();

        return Uri.fromFile(file);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        //Profile image upload
        profilePic = findViewById(R.id.profilePic);
        profileButton = findViewById(R.id.logo);
        save = findViewById(R.id.saveEdit);
        name = findViewById(R.id.name);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        gender = findViewById(R.id.gender);
        blood = findViewById(R.id.blood);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);


        //Profile image upload button
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });


        //Work next day




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


        //Insert value into room database
        save.setOnClickListener(v -> {

            AppDatabase db = AppDatabase.getDatabase(this);

            User user = db.userDao().getUser();
            boolean isNewUser = false;

            if (user == null) {
                user = new User();
                user.userId = 1;
                isNewUser = true;
            }

            if (!name.getText().toString().isEmpty()) {
                user.name = name.getText().toString();
            }

            if (!dateOfBirth.getText().toString().isEmpty()) {
                user.dateOfBirth = dateOfBirth.getText().toString();
            }

            if (!gender.getText().toString().isEmpty()) {
                user.gender = gender.getText().toString();
            }

            if (!blood.getText().toString().isEmpty()) {
                user.blood = blood.getText().toString();
            }

            if (!weight.getText().toString().isEmpty()) {
                user.weight = weight.getText().toString();
            }

            if (!height.getText().toString().isEmpty()) {
                user.height = height.getText().toString();
            }

            if (imgPath != null && !imgPath.isEmpty()) {
                user.image = imgPath;
            }


            db.userDao().insert(user);


            Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, home_dashboard_activity.class));
            finish();
        });




    }
}