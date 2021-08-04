package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

//        displayAppointments(getAppointmentIDsOnDate(apptDateCalendar.getTime()));
        displayAppointmentsOnDate(apptDateCalendar.getTime());
    }

    /*
    public ArrayList<String> getAppointmentIDsOnDate(Date date) { // Doesn't wait for Firebase to get data before returning, hence creation of displayAppointmentsonDate()
        ArrayList<String> apptIDs = new ArrayList<>();

        // Access Firebase to get Appointments
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Appointment appt = child.getValue(Appointment.class);
                    if (!appt.isBooked() && DateUtility.isSameDay(appt.getDate(), date)) {
                        apptIDs.add(appt.getAppointmentID());
                    }
                }
                Log.i("appt_id", apptIDs.size() + " size1");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error: no appointments found
            }
        });

        return apptIDs;
    } */

    /*
    public void displayAppointments(ArrayList<String> apptIDs) { // Uses appointment IDs instead of object
        if (apptIDs.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Error: no availabilities on that date", Toast.LENGTH_SHORT).show();
        } else {
            //Adding appointments to RadioGroup
            int index = 0;
            RadioGroup apptGroup = (RadioGroup) findViewById(R.id.appointmentRadioGroup);

            for (String apptID : apptIDs) {
                RadioButton appt = new RadioButton(this);
                appt.setText(apptID); // TODO: Instead of showing appointmentID, show data from the appointment
                apptGroup.addView(appt);
                appt.setId(index);
                appt.setPadding(0,0,0,16);
                index++;
            }
        }
    }
    public void displayAppointmentsOnDate(Date date) {
        ArrayList<String> apptIDs = new ArrayList<>();

        // Access Firebase to get Appointments
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Appointment appt = child.getValue(Appointment.class);
                    if (!appt.isBooked() && DateUtility.isSameDay(appt.getDate(), date)) {
                        apptIDs.add(appt.getAppointmentID());
                    }
                }

                displayAppointments(apptIDs);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error: no appointments found
                Toast.makeText(getApplicationContext(), "Error: couldn't find appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    public void displayAppointments(ArrayList<Appointment> appts) {
        if (appts.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Error: no availabilities on that date", Toast.LENGTH_SHORT).show();
        } else {
            //Adding appointments to RadioGroup
            int index = 0;
            RadioGroup apptGroup = (RadioGroup) findViewById(R.id.appointmentRadioGroup);

            for (Appointment appt : appts) {
                RadioButton apptRadioBtn = new RadioButton(this);
//                apptRadioBtn.setText(appt.getDoctorID());
//                apptRadioBtn.setText("\n");
                apptRadioBtn.setText(appt.getDate().toString()); // TODO: Instead of showing Doctor and full date here, just make TextViews above the radio group to show doctor name and date (only show time here)

                apptGroup.addView(apptRadioBtn);
                apptRadioBtn.setId(index);
                apptRadioBtn.setPadding(0,0,0,16);
                index++;
            }
        }
    }
    public void displayAppointmentsOnDate(Date date) { // TODO: Change to only show availabilities from Doctor that is passed from BookYourAppointmentsMain
        ArrayList<Appointment> appts = new ArrayList<>();

        // Access Firebase to get Appointments
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Appointment appt = child.getValue(Appointment.class);
                    if (!appt.isBooked() && DateUtility.isSameDay(appt.getDate(), date)) {
                        appts.add(appt);
                    }
                }

                displayAppointments(appts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error: no appointments found
                Toast.makeText(getApplicationContext(), "Error: couldn't find appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void onClickedBookAppointmentButton(View view){
        startActivity(new Intent(this, PatientAppointmentsView.class));
    }
}