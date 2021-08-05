package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DoctorViewAppointmentDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_appointment_details);
        Intent intentReceived = getIntent();
        String appointment = intentReceived.getStringExtra("Appointment");
        String[] appointmentDetails = appointment.split("\n");

        TextView patientNameView = findViewById(R.id.Patient_Name_Box);
        patientNameView.setText(appointmentDetails[0]);

        TextView appointmentDate = findViewById(R.id.Appointment_Time_Box);
        appointmentDate.setText(appointmentDetails[1]);
    }
}