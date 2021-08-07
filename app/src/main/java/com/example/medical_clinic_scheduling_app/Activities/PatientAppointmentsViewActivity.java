package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class PatientAppointmentsViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments_view);

        //Setting up ListView of Appointments
        ArrayList<String> appointments = new ArrayList<>();
        ListView appointmentsView = (ListView) findViewById(R.id.patientAppointmentListView);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //Find userID = patientID
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Find Appointments under that patientID
        ref.child(Constants.FIREBASE_PATH_APPOINTMENTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child != null) {
                        System.out.println(child.child(Constants.FIREBASE_PATH_APPOINTMENT_ID).getValue(String.class));
                        String patientID = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_PATIENT_ID).getValue(String.class);
                        String doctorID = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_DOCTOR_ID).getValue(String.class);
                        Date date = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_DATE).getValue(Date.class);
                        boolean passed = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_PASSED).getValue(Boolean.class);
                        boolean booked = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_BOOKED).getValue(Boolean.class);
                        if (patientID != null && patientID.equals(userID) && !passed && booked) {
                            ref.child(Constants.FIREBASE_PATH_USERS).addValueEventListener(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        String childID = child.child(Constants.FIREBASE_PATH_USERS_ID).getValue(String.class);
                                        if (doctorID.equals(childID)) {
                                            //Get Time of Now
                                            LocalDateTime timeNow = LocalDateTime.now();
                                            Date today = Date.from(timeNow.atZone(ZoneId.systemDefault()).toInstant());

                                            if (doctorID.equals(childID) && !date.before(today)) {
                                                Person doc = child.getValue(Person.class);
                                                appointments.add("Dr. " + doc.toString() + "\n" + date.toString());
                                            }
                                        }
                                    }
                                    ArrayAdapter appointmentAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, appointments);
                                    appointmentsView.setAdapter(appointmentAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}