package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class profile_activity extends AppCompatActivity {

    LinearLayout edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        edit = findViewById(R.id.editP);

        edit.setOnClickListener(v->{

            startActivity(new Intent(profile_activity.this, edit_profile_activity.class));

        });

    }
}