package com.example.medical_clinic_scheduling_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.medical_clinic_scheduling_app.Activities.Login.LoginViewActivity;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().getReference().removeValue();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMainLogin: // Login button
                startActivity(new Intent(this, LoginViewActivity.class));
                break;
            case R.id.btnMainRegister: // Register button
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}