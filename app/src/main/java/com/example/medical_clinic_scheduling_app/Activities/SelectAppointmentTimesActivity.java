package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Objects.Appointment;
import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.DateUtility;
import com.example.medical_clinic_scheduling_app.Fragments.DatePickerFragment;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SelectAppointmentTimesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Appointment selectedAppointment = null;
    private Map<Integer, Appointment> indexToAppt = new HashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    private SimpleDateFormat titleFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
    private static String doctor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Make TextViews at top of page to show doctor name and date

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointment_times);
        String doctorID = getIntent().getStringExtra("doctorID");
        if (getIntent().getStringExtra("doctor") != null){
            doctor = getIntent().getStringExtra("doctor");
        }

        //Setting up onCheckedChangeListener & showing selected appointment when clicked.
        RadioGroup appointmentGroup = (RadioGroup) findViewById(R.id.appointmentRadioGroup);
        appointmentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int index) {
                RadioButton selected = (RadioButton) appointmentGroup.getChildAt(index);
                Toast.makeText(getApplicationContext(),"Selected: " + selected.getText(),
                        Toast.LENGTH_SHORT).show();

                selectedAppointment = indexToAppt.get(selected.getId());
            }
        });

        // Open DatePicker
        DialogFragment datePicker = new DatePickerFragment(System.currentTimeMillis(), -1); // Disables selecting appointments in the past
        datePicker.show(getSupportFragmentManager(), "Appointment date"); // onDateSet() controls which appointments are shown
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        TextView title = findViewById(R.id.selectApptTitle);
        Calendar apptDateCalendar = Calendar.getInstance();
        apptDateCalendar.set(Calendar.YEAR, year);
        apptDateCalendar.set(Calendar.MONTH, month);
        apptDateCalendar.set(Calendar.DAY_OF_MONTH, day);
        apptDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        apptDateCalendar.set(Calendar.MINUTE, 0);
        apptDateCalendar.set(Calendar.SECOND, 0);
        apptDateCalendar.set(Calendar.MILLISECOND, 0);
        title.setText(doctor.substring(0, doctor.indexOf("\n")) + "'s available\nappointments on\n" + titleFormat.format(apptDateCalendar.getTime()));
        displayAppointmentsOnDate(apptDateCalendar.getTime());
    }

    public void displayAppointments(ArrayList<Appointment> appts) {
        if (appts.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Error: no availabilities on that date", Toast.LENGTH_SHORT).show();
        } else {
            //Adding appointments to RadioGroup
            int index = 0;
            RadioGroup apptGroup = (RadioGroup) findViewById(R.id.appointmentRadioGroup);
            apptGroup.removeAllViews();

            indexToAppt.clear(); // Reset just in case old appts
            for (Appointment appt : appts) {
                indexToAppt.put(index, appt);

                RadioButton apptRadioBtn = new RadioButton(this);
                apptRadioBtn.setText(sdf.format(appt.getDate()));

                apptGroup.addView(apptRadioBtn);
                apptRadioBtn.setId(index);
                apptRadioBtn.setPadding(0,0,0,16);
                index++;
            }
        }
    }
    public void displayAppointmentsOnDate(Date date) { // Only show availabilities from Doctor that is passed from BookYourAppointmentsMain
        ArrayList<Appointment> appts = new ArrayList<>();
        String doctorID = getIntent().getStringExtra("doctorID");

        // Get doctor's availableAppointmentIDs
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .child(doctorID)
                .child(Constants.FIREBASE_PATH_USERS_APPTS_AVAILABLE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot availableApptIDsSnapshot) {
                        for (DataSnapshot availableApptIDChild : availableApptIDsSnapshot.getChildren()) { // Loop through all of doctor's availableApptIDs
                            String availableApptID = availableApptIDChild.getValue(String.class);

                            // For each availableAppointmentID, get the Appointment object
                            FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS)
                                    .child(availableApptID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot availableApptSnapshot) {
                                            Appointment appt = availableApptSnapshot.getValue(Appointment.class);
                                            if (!appt.isBooked() && DateUtility.isSameDay(appt.getDate(), date)) {
                                                appts.add(appt);
                                                displayAppointments(appts);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { // Error: Appointment associated w/ availableAppointmentID not found
                                            Toast.makeText(getApplicationContext(), "Error: Couldn't find available appointment", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { // Error: No availableAppointmentIDs found
                        Toast.makeText(getApplicationContext(), "Error: No available appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickedBookAppointmentButton(View view){
        if (this.selectedAppointment != null) {
            String patientID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // TODO: Pass from previous activities to prevent errors
            Appointment.bookAppointment(this.selectedAppointment, patientID);

            Toast.makeText(getApplicationContext(), "Booked appointment", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, PatientAppointmentsViewActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Error: select an appointment", Toast.LENGTH_SHORT).show();
        }
    }
}