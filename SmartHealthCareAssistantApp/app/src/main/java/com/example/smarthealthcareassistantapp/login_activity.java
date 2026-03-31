package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class login_activity extends AppCompatActivity {

    Button login,signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Sign Up button to access register activity
        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(v -> {
            startActivity(new Intent(login_activity.this, sign_up_Activity.class));
        });

        login = findViewById(R.id.signIn);

        login.setOnClickListener(v -> {
            startActivity(new Intent(login_activity.this, home_dashboard_activity.class));
        });



    }
}