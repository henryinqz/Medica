package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewPreviousAppointments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_previous_appointments);

        ListView previousAppointmentsView = (ListView) findViewById(R.id.List_of_Previous_Appointments);
        ArrayList<String> previousAppointments = new ArrayList<String>();
        previousAppointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        previousAppointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        previousAppointments.add("patient\n Aug 7, 2021 @ 1pm-3pm");
        ArrayAdapter previousAppointmentsAdaptor = new ArrayAdapter(this, android.R.layout.simple_list_item_1, previousAppointments);
        previousAppointmentsView.setAdapter(previousAppointmentsAdaptor);

        previousAppointmentsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DoctorViewAppointmentDetailsActivity.class);
                intent.putExtra("Appointment", previousAppointments.get(i));
                startActivity(intent);
            }
        });
    }
}