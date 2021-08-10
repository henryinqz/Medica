package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Objects.Appointment;
import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Doctor;
import com.example.medical_clinic_scheduling_app.Fragments.MultipleSelectionFragment;
import com.example.medical_clinic_scheduling_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class DoctorRegisterActivity extends AppCompatActivity {
    private HashSet<String> specializations = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        //Setting up the Gender Dropdown List
        Spinner genderSpinner = (Spinner)findViewById(R.id.spinnerRegisterDoctorGender);
        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<String>(DoctorRegisterActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.SelectGender));
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderSpinnerAdapter);

        // Specialist dialog list
        Button btnSelectSpecialist = (Button) findViewById(R.id.Button_Select_Specialist);
        btnSelectSpecialist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MultipleSelectionFragment selectionDialog = new MultipleSelectionFragment(getResources().getStringArray(R.array.SelectSpecialist), "Select your specializations");
                selectionDialog.show(getSupportFragmentManager(), "SelectSpecialistDialog");
                specializations = selectionDialog.getSelectedItems();
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegisterDoctorSubmit: // Submit button
                registerDoctor();
                break;
        }
    }

    private void registerDoctor() {
        // Name
        EditText firstNameEditText = (EditText) findViewById(R.id.editTextRegisterDoctorFirstName);
        EditText lastNameEditText = (EditText) findViewById(R.id.editTextRegisterDoctorLastName);
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        // Username
        EditText usernameEditText = (EditText) findViewById(R.id.editTextRegisterDoctorUsername);
        String username = usernameEditText.getText().toString().trim();

        // Password
        EditText passwordEditText = (EditText) findViewById(R.id.editTextRegisterDoctorPassword);
        String password = passwordEditText.getText().toString().trim();

        Spinner genderSpinner = (Spinner) findViewById(R.id.spinnerRegisterDoctorGender);
        String gender = genderSpinner.getSelectedItem().toString();

        // Errors
        if(firstName.isEmpty()) {
            firstNameEditText.setError("Empty first name");
            return;
        }
        if(lastName.isEmpty()) {
            firstNameEditText.setError("Empty last name");
            return;
        }
        if (username.isEmpty()) {
            usernameEditText.setError("Empty username");
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Empty password");
            return;
        }

        String emailUsername = username + Constants.USERNAME_EMAIL_DOMAIN;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(emailUsername, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Doctor user = new Doctor(username, firstName, lastName, gender, specializations, userUid);

                    //Appointment.generateAvailableAppointment(new Date(System.currentTimeMillis() + 600000), user); // TODO: Broken (in method)

                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(userUid)
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) { // Created user
                                        Toast.makeText(getApplicationContext(), "Registered doctor", Toast.LENGTH_LONG).show();

                                        // Login (User is authenticated already (?))
                                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        // Redirect to home page
                                        Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                                        intent.putExtra("userid", userID);
                                        createAppointments(user);
                                        startActivity(intent);
                                        finish();
                                    } else { // Failed to create user
                                        Toast.makeText(getApplicationContext(), "Failed to register doctor", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else { // Failed to create user
                    Toast.makeText(getApplicationContext(), "Failed to create doctor", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void createAppointments(Doctor user){
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 7; i++) { //Generating 1 week's worth of appts.
            Appointment.generateAvailableAppointmentsForOneDoctor(user, c.getTime());
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}