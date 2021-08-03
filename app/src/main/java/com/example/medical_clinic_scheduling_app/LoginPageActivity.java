package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

                    String userID = auth.getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                    ref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Person user = snapshot.getValue(Person.class);

                            if (user != null) {
                                Toast.makeText(getApplicationContext(), user.getType(), Toast.LENGTH_LONG).show();
                                if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) { // Doctor
                                    Toast.makeText(getApplicationContext(), "Hello doctor " + user.getUsername(), Toast.LENGTH_LONG).show();
                                    // TODO: Redirect to doctor page
                                    startActivity(new Intent(getApplicationContext(), DoctorViewAppointmentActivity.class));
                                } else if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)) { // Patient
                                    Toast.makeText(getApplicationContext(), "Hello patient " + user.getUsername(), Toast.LENGTH_LONG).show();
                                    // TODO: Redirect to patient page
                                    startActivity(new Intent(getApplicationContext(),PatientAppointmentsView.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error: user has no type", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Error: user is null", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Error logging in", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // Failed to login
                    Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}