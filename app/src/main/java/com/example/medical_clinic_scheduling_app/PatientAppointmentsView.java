package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class PatientAppointmentsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments_view);

        //Setting up ListView of Appointments
        ArrayList<String> appointments = new ArrayList<>();
        ListView appointmentsView = (ListView) findViewById(R.id.patientAppointmentListView);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //Find userID = patientID
        String userID = getIntent().getStringExtra("userid");
        //Find Appointments under that patientID
        ref.child("Appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String patientID = child.child("patientID").getValue(String.class);
                    String doctorID = child.child("doctorID").getValue(String.class);
                    System.out.println("doctorid" + " " + doctorID + "\n" + "patientID: " + patientID);
                    Date date = child.child("date").getValue(Date.class);
                    if (patientID.equals(userID)){
                        System.out.println("hi");
                        ref.child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    String childID = child.child("id").getValue(String.class);
                                    System.out.println("childID: " + childID);
                                    if (doctorID.equals(childID)){
                                        System.out.println("hi2");
                                        Person doc = child.getValue(Person.class);
                                        System.out.println(doc.toString());
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
        Intent intent = new Intent(this, BookYourAppointmentMain.class);
        startActivity(intent);
    }
}