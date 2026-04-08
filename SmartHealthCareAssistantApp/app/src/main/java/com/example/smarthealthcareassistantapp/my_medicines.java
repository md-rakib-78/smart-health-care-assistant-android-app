package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class my_medicines extends AppCompatActivity {

    Button addMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_medicines);

        addMed = findViewById(R.id.add);

        addMed.setOnClickListener(v->{
            startActivity(new Intent(this,add_medicine.class));
        });



    }
}