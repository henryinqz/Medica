package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginSubmit: // Submit button
                loginUser();
                break;
        }
    }

    private void loginUser() {
        // Username
        EditText usernameEditText = (EditText) findViewById(R.id.editTextLoginUsername);
        String username = usernameEditText.getText().toString().trim();

        // Password
        EditText passwordEditText = (EditText) findViewById(R.id.editTextLoginPassword);
        String password = passwordEditText.getText().toString().trim();

        // Errors
        if (username.isEmpty()) {
            usernameEditText.setError("Empty username");
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Empty password");
            return;
        }

        String emailUsername = username + "@example.com";
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(emailUsername, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // Logged in
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                    // TODO: Start next activity
                } else {
                    // Failed to login
                    Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}