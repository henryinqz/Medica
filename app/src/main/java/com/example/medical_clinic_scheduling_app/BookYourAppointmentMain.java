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

public class BookYourAppointmentMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_your_appointment_main);

        //Setting up ListView of Doctors
        ListView doctorView = (ListView) findViewById(R.id.doctorListView);
        ArrayList<String> doctors = new ArrayList<>(); //change to Doctor type & make toString() method.
        //The format can be: "Dr. NAME\nGENDER\nSPECIALIZATION"
        doctors.add("Dr. Eric Zhou\nMale\nCardiology");
        doctors.add("Dr. Eric Zhou\nMale\nCardiology");
        doctors.add("Dr. Eric Zhou\nMale\nCardiology");
        doctors.add("Dr. Eric Zhou\nMale\nCardiology");
        doctors.add("Dr. Eric Zhou\nMale\nCardiology");

        ArrayAdapter doctorAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, doctors);
        doctorView.setAdapter(doctorAdapter);

        //Setting up listener for when item is clicked.
        doctorView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BookYourAppointmentMain.this, "Selected Doctors: " + doctors.get(i).toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SelectAppointmentTimes.class));
            }
        });
    }

    public void onFilterStartButtonClicked (View view){
        //TODO: filter database w/ selected options and return result to main page

        Intent intent = new Intent(this, AppointmentFilterOptions.class);
        startActivity(intent);
    }
}