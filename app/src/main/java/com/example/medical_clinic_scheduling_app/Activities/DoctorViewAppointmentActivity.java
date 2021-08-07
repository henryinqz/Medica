package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class DoctorViewAppointmentActivity extends AppCompatActivity {

    protected static String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_appointment);

        //Setting up ListView of Appointments
        ArrayList<String> appointments = new ArrayList<>();
        ListView appointmentsView = (ListView) findViewById(R.id.AppointmentListView);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //Find userID = doctorID
        if (getIntent().getStringExtra("userid") != null) {
            userID = getIntent().getStringExtra("userid");
        }

        //Find Appointments under that doctorID
        ref.child(Constants.FIREBASE_PATH_APPOINTMENTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String patientID = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_PATIENT_ID).getValue(String.class);
                    String doctorID = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_DOCTOR_ID).getValue(String.class);
                    Date date = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_DATE).getValue(Date.class);
                    if (doctorID.equals(userID)){
                        ref.child(Constants.FIREBASE_PATH_USERS).addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    String childID = child.child(Constants.FIREBASE_PATH_USERS_ID).getValue(String.class);
                                    //Get Time of Now
                                    LocalDateTime timeNow = LocalDateTime.now();
                                    Date today = Date.from(timeNow.atZone(ZoneId.systemDefault()).toInstant());

                                    if (patientID.equals(childID) && !date.before(today)) {
                                        Person patient = child.getValue(Person.class);
                                        appointments.add("Patient " + patient.toString() + "\n" + date.toString());
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(appointmentsView != null) {
            appointmentsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), DoctorViewAppointmentDetailsActivity.class);
                    intent.putExtra("Appointment", appointments.get(i));
                    startActivity(intent);
                }
            });
        }
    }
}