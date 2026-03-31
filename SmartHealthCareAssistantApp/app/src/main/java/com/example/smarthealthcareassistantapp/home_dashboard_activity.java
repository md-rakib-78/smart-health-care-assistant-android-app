package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class home_dashboard_activity extends AppCompatActivity {

    LinearLayout profileicon1,profileicon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_dashboard);

        profileicon1 = findViewById(R.id.profileIcon);
        profileicon2 = findViewById(R.id.profileIcon2);

        profileicon1.setOnClickListener(v->{

            startActivity(new Intent(home_dashboard_activity.this, profile_activity.class));

        });

        profileicon2.setOnClickListener(v->{
            startActivity(new Intent(home_dashboard_activity.this, profile_activity.class));
        });

    }
}