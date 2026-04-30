package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import database.AppDatabase;
import database.User;

public class profile_activity extends AppCompatActivity {

    LinearLayout edit;
    ImageView profilePic;
    TextView nameText,age,weight,blood,dateOfBirth,gender,height;
    Button signOut;




        public  String calculateAge(String dobString) {

            //
            if (dobString == null || dobString.trim().isEmpty()) {
                return "-";
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

                LocalDate dob = LocalDate.parse(dobString.trim(), formatter);
                LocalDate today = LocalDate.now();

                Period age = Period.between(dob, today);

                return age.getYears() + " years";

            } catch (Exception e) {
                e.printStackTrace();
                return "Invalid date";
            }
        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        edit = findViewById(R.id.editP);
        profilePic = findViewById(R.id.profilePic);
        nameText = findViewById(R.id.logoText);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        blood = findViewById(R.id.blood);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        gender = findViewById(R.id.gender);
        height = findViewById(R.id.height);
        signOut = findViewById(R.id.signOut);

        signOut.setOnClickListener(v->{

            Intent intent = new Intent(this, login_activity.class);
            startActivity(intent);
            finish();

        });


        edit.setOnClickListener(v->{

            startActivity(new Intent(profile_activity.this, edit_profile_activity.class));
        });

        //User Detail in dashboard (User name, profile)
        AppDatabase db = AppDatabase.getDatabase(this);
        User user = db.userDao().getUser();

        new Thread(() -> {

            runOnUiThread(() -> {

                if (user != null) {
                    nameText.setText(user.name);
                    if (user.image != null) {
                        File file = new File(user.image);
                        if (file.exists()) {
                            profilePic.setImageURI(Uri.fromFile(file));
                        }
                    }


                    String Age = calculateAge(user.dateOfBirth);
                    age.setText(Age);
                    weight.setText(user.weight+" kg");
                    blood.setText(user.blood);
                    dateOfBirth.setText(user.dateOfBirth);
                    gender.setText(user.gender);
                    height.setText(user.height+" cm");
                }

            });

        }).start();


    }
}