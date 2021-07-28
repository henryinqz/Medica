package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void on_login(View view) {

    }

    public void loginButtonClick(View view) { // Run when login button is pressed
        // Username
        EditText usernameEditText = (EditText) findViewById(R.id.editTextLoginUsername);
        String username = usernameEditText.getText().toString();

        // Password
        EditText passwordEditText = (EditText) findViewById(R.id.editTextLoginPassword);
        String password = passwordEditText.getText().toString();


        if (username.isEmpty() || password.isEmpty()) { // Login failed: empty username/password
            if (username.isEmpty())
                usernameEditText.setError("Empty username");
            if (password.isEmpty())
                passwordEditText.setError("Empty password");
//        } else if () { // TODO: Regex to check if username & passwords have correct format (ie. no whitespace, dealing w/ special characters, length of 32 char or smth)
        } else {
            // TODO: Check that username/password combo is correct w/ Firebase

            // TODO: Login (auth token? then go to patient/doctor screen of upcoming appointments)

        }
    }
}