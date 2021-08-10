package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Fragments.DatePickerFragment;
import com.example.medical_clinic_scheduling_app.Objects.Patient;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class PatientRegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Date dateOfBirth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        //Setting up the Gender Dropdown List
        Spinner genderSpinner = (Spinner)findViewById(R.id.spinnerRegisterPatientGender);
        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<String>(PatientRegisterActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.SelectGender));
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderSpinnerAdapter);

        //Setting up user birthDateButton Listener
        Button birthDate = (Button) findViewById(R.id.btnRegisterPatientBirthdaySelect);
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new DatePickerFragment(-1, System.currentTimeMillis()); // Disables selecting birthday in the future
                datePicker.show(getSupportFragmentManager(), "Birth Date Picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        //Setting the birthDateLabel to patient's birthday from DatePicker.
        Calendar birthDateCalendar = Calendar.getInstance();
        birthDateCalendar.set(Calendar.YEAR, year);
        birthDateCalendar.set(Calendar.MONTH, month);
        birthDateCalendar.set(Calendar.DAY_OF_MONTH, day);
        birthDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        birthDateCalendar.set(Calendar.MINUTE, 0);
        birthDateCalendar.set(Calendar.SECOND, 0);
        birthDateCalendar.set(Calendar.MILLISECOND, 0);

        String currentDateStr = DateFormat.getDateInstance().format(birthDateCalendar.getTime());
        TextView textView = (TextView) findViewById(R.id.txtRegisterPatientBirthdayDate);
        textView.setText(currentDateStr);

        dateOfBirth = birthDateCalendar.getTime();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegisterPatientSubmit: // Submit button
                registerPatient();
                break;
        }
    }

    private void registerPatient() {
        // Name
        EditText firstNameEditText = (EditText) findViewById(R.id.editTextRegisterPatientFirstName);
        EditText lastNameEditText = (EditText) findViewById(R.id.editTextRegisterPatientLastName);
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        // Username
        EditText usernameEditText = (EditText) findViewById(R.id.editTextRegisterPatientUsername);
        String username = usernameEditText.getText().toString().trim();

        // Password
        EditText passwordEditText = (EditText) findViewById(R.id.editTextRegisterPatientPassword);
        String password = passwordEditText.getText().toString().trim();

        Spinner genderSpinner = (Spinner) findViewById(R.id.spinnerRegisterPatientGender);
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
        if (this.dateOfBirth == null) {
            Toast.makeText(getApplicationContext(), "Empty date of birth", Toast.LENGTH_LONG).show();
            return;
        }

        String emailUsername = username + Constants.USERNAME_EMAIL_DOMAIN;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(emailUsername, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Person user = new Patient(username, firstName, lastName, gender, dateOfBirth, userUid);

                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(userUid)
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) { // Created user
                                        Toast.makeText(getApplicationContext(), "Registered patient", Toast.LENGTH_LONG).show();

                                        // Login (User is authenticated already (?))
                                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        // Redirect to home page
                                        Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                                        intent.putExtra("userid", userID);
                                        startActivity(intent);
                                        finish();
                                    } else { // Failed to create user
                                        Toast.makeText(getApplicationContext(), "Failed to create patient", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else { // Failed to create user
                    Toast.makeText(getApplicationContext(), "Failed to create patient", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}