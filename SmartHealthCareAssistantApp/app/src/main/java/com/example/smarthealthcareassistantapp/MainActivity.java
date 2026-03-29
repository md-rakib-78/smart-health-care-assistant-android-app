package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button getS,signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Status Bar Icon, Text, Color
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            controller.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }

        // Buttons
        getS = findViewById(R.id.getStartedButton);
        signIn = findViewById(R.id.signInButton);

        // On Click Listener
        getS.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, home_dashboard_activity.class));
            finish();
        });

        signIn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, login_activity.class));
            finish();
        });
    }


}