package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BookYourAppointmentMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_your_appointment_main);
    }
    public void onFilterButtonClicked (View view){
        Intent intent = new Intent(this, AppointmentFilterOptions.class);
        startActivity(intent);
    }
}