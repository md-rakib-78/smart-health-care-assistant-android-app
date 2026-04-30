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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import database.AppDatabase;
import database.User;

public class sign_up_Activity extends AppCompatActivity {

    private EditText fullName,email,password,Cpassword;

    private Button signUp;

    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        Cpassword = findViewById(R.id.Cpass);
        signUp = findViewById(R.id.signup);
        progressBar = findViewById(R.id.prog);


        auth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(v -> {

            String fname = fullName.getText().toString();
            String mail = email.getText().toString();
            String pass = password.getText().toString();
            String Cpass = Cpassword.getText().toString();



            if (fname.isEmpty()) {
                fullName.setError("Full name is required");
                fullName.requestFocus();
                return;
            }
            if (mail.isEmpty()) {
                email.setError("Email is required");
                email.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                email.setError("Enter valid email");
                email.requestFocus();
                return;
            }


            if (pass.isEmpty()) {
                password.setError("Password is required");
                password.requestFocus();
                return;
            }

            if (pass.length() < 6) {
                password.setError("Password must be at least 6 characters");
                password.requestFocus();
                return;
            }

            if (!pass.equals(Cpass)) {
                Cpassword.setError("Password does not match");
                Cpassword.requestFocus();
                return;
            }

            // Show loading
            progressBar.setVisibility(View.VISIBLE);
            signUp.setText(""); // hide text
            signUp.setEnabled(false);


            // Call Firebase
            auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    // Hide loading (after task done)
                    progressBar.setVisibility(View.GONE);
                    signUp.setText("Sign Up");
                    signUp.setEnabled(true);

                    Toast.makeText(sign_up_Activity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, home_dashboard_activity.class));

                    //Insert data into room database
                    AppDatabase db = AppDatabase.getDatabase(this);
                    String Fname = fullName.getText().toString();
                    String Mail = email.getText().toString();
                    String Pass = password.getText().toString();

                    User user = db.userDao().getUser();

                    if (user == null) {
                        user = new User();
                        user.userId = 1;
                    }

                    user.name = Fname;
                    user.email = Mail;
                    user.password = Pass;

                    db.userDao().insert(user);

                    finish();

                }else{
                    Toast.makeText(sign_up_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(sign_up_Activity.this, "User already exists", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(sign_up_Activity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            });





        });



    }
}