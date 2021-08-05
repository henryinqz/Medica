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

public class DoctorViewAppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_appointment);

//        //Setting up ListView of Appointments
        ListView upcomingAppointmentsView = (ListView) findViewById(R.id.AppointmentListView);
        ArrayList<String> upcomingAppointments = new ArrayList<String>();
        upcomingAppointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        upcomingAppointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        upcomingAppointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        upcomingAppointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        upcomingAppointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        ArrayAdapter upcomingAppointmentsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, upcomingAppointments);
        upcomingAppointmentsView.setAdapter(upcomingAppointmentsAdapter);

        upcomingAppointmentsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DoctorViewAppointmentDetailsActivity.class);
                intent.putExtra("Appointment", upcomingAppointments.get(i));
                startActivity(intent);
            }
        });
    }

    public void gotoViewPreviousAppointmentsPage(View view){
        startActivity(new Intent(getApplicationContext(), ViewPreviousAppointments.class));
    }

    public void gotoViewAvailableTimeSlotsPage(View view){
        startActivity(new Intent(getApplicationContext(), DoctorViewAvailableTimeSlotsActivity.class));
    }
}