package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PatientAppointmentsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments_view);

        //Setting up ListView of Appointments
        ListView appointmentsView = (ListView) findViewById(R.id.patientAppointmentListView);
        ArrayList<String> appointments = new ArrayList<>();
        appointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        appointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        appointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        appointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        appointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");

        ArrayAdapter appointmentAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, appointments);
        appointmentsView.setAdapter(appointmentAdapter);
    }

    public void onBookAppBtnClicked (View view){
        Intent intent = new Intent(this, BookYourAppointmentMain.class);
        startActivity(intent);
    }
}