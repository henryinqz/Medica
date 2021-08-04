package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
        // TODO: Gender, specialization error checks (?) (Shouldn't be errors but maybe check if the strings match something expected input?)
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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(username + "@example.com", password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    Person user = new Doctor(username, firstName, lastName, gender, specializations, userUid);
                    Doctor user = new Doctor(username, firstName, lastName, gender, specializations, userUid);

//                    // TEMP: Add misc appointments to Firebase
//                    Appointment appt = new Appointment(new Date(System.currentTimeMillis()), (Doctor) user, null);
//
//                    FirebaseDatabase.getInstance().getReference("Appointments")
//                            .child(appt.getAppointmentID())
//                            .setValue(appt).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) { // Added appointment
//                                user.addAvailableAppointment(appt);
//                            } else { // Failed to add appointment
//                                Toast.makeText(getApplicationContext(), "Failed to create doctor appt", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });


                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(userUid)
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) { // Created user
                                        Toast.makeText(getApplicationContext(), "Created doctor user", Toast.LENGTH_LONG).show();
                                        // TODO: Login & go to next intent
                                    } else { // Failed to create user
                                        Toast.makeText(getApplicationContext(), "Failed to create doctor", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else { // Failed to create user
                    Toast.makeText(getApplicationContext(), "Failed to create doctor", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}