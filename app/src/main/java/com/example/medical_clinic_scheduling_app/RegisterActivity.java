package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void navigateToDoctorRegister(View view) {
        Intent intent = new Intent(this, DoctorRegisterActivity.class);
        startActivity(intent);
    }

    public void navigateToPatientRegister(View view){
        Intent intent = new Intent(this, PatientRegisterActivity.class);
        startActivity(intent);
    }
}