package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class PatientAppointmentsViewActivity extends AppCompatActivity {

    protected static String userID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments_view);

        //Setting up ListView of Appointments
        ArrayList<String> appointments = new ArrayList<>();
        ListView appointmentsView = (ListView) findViewById(R.id.patientAppointmentListView);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //Find userID = patientID
        if (getIntent().getStringExtra("userid") != null) {
            userID = getIntent().getStringExtra("userid");
        }
        //Find Appointments under that patientID
        ref.child(Constants.FIREBASE_PATH_APPOINTMENTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String patientID = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_PATIENT_ID).getValue(String.class);
                    String doctorID = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_DOCTOR_ID).getValue(String.class);
                    Date date = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_DATE).getValue(Date.class);
                    if (patientID.equals(userID)){
                        ref.child(Constants.FIREBASE_PATH_USERS).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    String childID = child.child(Constants.FIREBASE_PATH_USERS_ID).getValue(String.class);
                                    if (doctorID.equals(childID)){
                                        Person doc = child.getValue(Person.class);
                                        appointments.add("Dr. " + doc.toString() + "\n" + date.toString());
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
    }

    public void onBookAppBtnClicked (View view){
        Intent intent = new Intent(this, BookYourAppointmentMainActivity.class);
        startActivity(intent);
    }
}