package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_activity extends AppCompatActivity {

    private Button login,signUp;
    private EditText email,password;
    FirebaseAuth mAuth;
    private ProgressBar progressBar;




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
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        progressBar = findViewById(R.id.prog);


        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {

            String Email = email.getText().toString().trim();
            String Password = password.getText().toString().trim();

            if(Email.isEmpty()){
                email.setError("Email is required");
                email.requestFocus();
                return;
            }

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                email.setError("Enter valid email");
                email.requestFocus();
                return;
            }

            if(Password.isEmpty()) {
                password.setError("Password is required");
                password.requestFocus();
                return;
            }

            if(Password.length() < 6){
                password.setError("Password must be at least 6 characters");
                password.requestFocus();
                return;
            }

            // Show loading
            progressBar.setVisibility(View.VISIBLE);
            login.setText(""); // hide text
            login.setEnabled(false);



            mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    // Hide loading (after task done)
                    progressBar.setVisibility(View.GONE);
                    login.setText("Sign In");
                    login.setEnabled(true);

                        startActivity(new Intent(login_activity.this, home_dashboard_activity.class));
                        finish();
                }else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(login_activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        });



    }
}