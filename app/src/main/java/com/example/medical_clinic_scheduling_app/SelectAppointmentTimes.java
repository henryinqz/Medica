package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class SelectAppointmentTimes extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Appointment selectedAppointment = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MM dd yyyy @hh");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointment_times);

        //Setting up onCheckedChangeListener & showing selected appointment when clicked.
        RadioGroup appointmentGroup = (RadioGroup) findViewById(R.id.appointmentRadioGroup);
        appointmentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selected = (RadioButton) appointmentGroup.getChildAt(i);
                Toast.makeText(getApplicationContext(),"Selected: " + selected.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Open DatePicker
        DialogFragment datePicker = new DatePickerFragment(System.currentTimeMillis(), -1); // Disables selecting appointments in the past
        datePicker.show(getSupportFragmentManager(), "Appointment date");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        Calendar apptDateCalendar = Calendar.getInstance();
        apptDateCalendar.set(Calendar.YEAR, year);
        apptDateCalendar.set(Calendar.MONTH, month);
        apptDateCalendar.set(Calendar.DAY_OF_MONTH, day);
        apptDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        apptDateCalendar.set(Calendar.MINUTE, 0);
        apptDateCalendar.set(Calendar.SECOND, 0);
        apptDateCalendar.set(Calendar.MILLISECOND, 0);

        displayAppointments(getAppointmentIDsOnDate(apptDateCalendar.getTime()));
    }

    public ArrayList<String> getAppointmentIDsOnDate(Date date) {
        ArrayList<String> apptIDs = new ArrayList<>();

        // Access Firebase to get Appointments
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Appointments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Appointment appt = child.getValue(Appointment.class);
                    if (!appt.isBooked() && DateUtility.isSameDay(appt.getDate(), date)) {
                        apptIDs.add(appt.getAppointmentID());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error: no appointments found
                Toast.makeText(getApplicationContext(), "No appointments found on that date", Toast.LENGTH_SHORT).show();
            }
        });

        return apptIDs;
    }

    public void displayAppointments(ArrayList<String> apptIDs) {
        if (apptIDs.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No appointments found on that date", Toast.LENGTH_SHORT).show();
        } else {
            //Adding appointments to RadioGroup
            int index = 0;
            RadioGroup apptGroup = (RadioGroup) findViewById(R.id.appointmentRadioGroup);

            for (String apptID : apptIDs) {
                RadioButton appt = new RadioButton(this);
                appt.setText(apptID);
                apptGroup.addView(appt);
                appt.setId(index);
                appt.setPadding(0,0,0,16);
                index++;
            }
        }
    }

    public void onClickedBookAppointmentButton(View view){
        startActivity(new Intent(this, PatientAppointmentsView.class));
    }
}