package com.example.medical_clinic_scheduling_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.medical_clinic_scheduling_app.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegisterPatient: // Login button
                startActivity(new Intent(this, PatientRegisterActivity.class));
                break;
            case R.id.btnRegisterDoctor: // Register button
                startActivity(new Intent(this, DoctorRegisterActivity.class));
                break;
        }
    }
}