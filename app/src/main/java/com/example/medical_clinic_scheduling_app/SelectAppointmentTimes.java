package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class SelectAppointmentTimes extends AppCompatActivity {

    private Appointment selectedAppointment = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MM dd yyyy @hh");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointment_times);

        //Setting up RadioGroup of Appointments
        RadioGroup appointmentGroup = (RadioGroup) findViewById(R.id.appointmentRadioGroup);
        ArrayList<String> appointments = new ArrayList<>();
        appointments.add("Date: Tuesday July 27, 2021\nTime: 12:00a.m - 1:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 1:00p.m - 2:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 2:00p.m - 3:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 3:00p.m - 4:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 12:00a.m - 1:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 1:00p.m - 2:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 2:00p.m - 3:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 3:00p.m - 4:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 12:00a.m - 1:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 1:00p.m - 2:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 2:00p.m - 3:00p.m");
        appointments.add("Date: Tuesday July 27, 2021\nTime: 3:00p.m - 4:00p.m");

        //Adding appointments to RadioGroup
        int i = 0;
        RadioButton appointment;
        for (String s: appointments) {
            appointment = new RadioButton(this);
            appointment.setText(s);
            appointmentGroup.addView(appointment);
            appointment.setId(i);
            appointment.setPadding(0,0,0,16);
            i++;
        }

        //Setting up onCheckedChangeListener & showing selected appointment when clicked.
        appointmentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selected = (RadioButton) appointmentGroup.getChildAt(i);
                Toast.makeText(getApplicationContext(),"Selected: " + selected.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onClickedBookAppointmentButton(View view){
        startActivity(new Intent(this, PatientAppointmentsView.class));
    }
}