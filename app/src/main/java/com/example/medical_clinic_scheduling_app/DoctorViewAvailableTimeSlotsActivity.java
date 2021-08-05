package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DoctorViewAvailableTimeSlotsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_available_time_slots);

        ListView availableTimesView = (ListView) findViewById(R.id.List_of_Available_Time_Slots);
        ArrayList<String> availableTimes = new ArrayList<String>();
        availableTimes.add("Aug 9, 2021 @ 1pm-3pm");
        availableTimes.add("Aug 9, 2021 @ 1pm-3pm");
        availableTimes.add("Aug 9, 2021 @ 1pm-3pm");
        availableTimes.add("Aug 9, 2021 @ 1pm-3pm");
        ArrayAdapter availableTimesAdaptor = new ArrayAdapter(this, android.R.layout.simple_list_item_1, availableTimes);
        availableTimesView.setAdapter(availableTimesAdaptor);

//        availableTimesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Intent intent = new Intent(getApplicationContext(), DoctorViewAppointmentDetailsActivity.class);
////                intent.putExtra("Appointment", availableTimes.get(i));
////                startActivity(intent);
//            }
//        });
    }
}