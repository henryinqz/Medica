package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectAppointmentTimes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointment_times);

        //Setting up ListView of Appointments
        ListView appointmentView = (ListView) findViewById(R.id.appointmentListView);
        ArrayList<String> appointments = new ArrayList<>();
        //Can choose different format
        appointments.add("Tuesday, August 3 2021 @12-2pm");
        appointments.add("Wednesday, August 4 2021 @1-3pm");
        appointments.add("Thursday, August 5 2021 @4-5pm");
        appointments.add("Friday, August 6 2021 @7-8pm");
        appointments.add("Saturday, August 7 2021 @9-10am");

        ArrayAdapter appointmentAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, appointments);
        appointmentView.setAdapter(appointmentAdapter);

        //Setting up listener for when item is clicked.
        appointmentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectAppointmentTimes.this, "Selected Appointment: " + appointments.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickedBookAppointmentButton(){
        //TODO: go to main page showing new selected appointment
    }
}