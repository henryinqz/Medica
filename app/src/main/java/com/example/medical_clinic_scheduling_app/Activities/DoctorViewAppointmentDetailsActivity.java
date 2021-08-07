package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class DoctorViewAppointmentDetailsActivity extends AppCompatActivity {

    private String thisPatientName = null;
    private Date thisAppointmentDate = null;
    private String thisAppointmentID = null;
    private String thisDoctorName = null;
    private String thisPatientId = null;
    private String thisPatientGender = null;
    private Date thisPatientBirthday = null;
    private ArrayList<String> previousDoctorSeen = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_appointment_details);
        Intent intentReceived = getIntent();
        thisAppointmentID = intentReceived.getStringExtra("Appointment");

        TextView patientGenderView = findViewById(R.id.Patient_Gender_Box);
        TextView doctorNameView = findViewById(R.id.Doctor_Name_Box);
        TextView appointmentIDView = findViewById(R.id.Appointment_ID_Box);
        TextView appointmentDateView = findViewById(R.id.Appointment_Time_Box);
        TextView patientNameView = findViewById(R.id.Patient_Name_Box);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(Constants.FIREBASE_PATH_APPOINTMENTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String patientID = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_PATIENT_ID).getValue(String.class);
                    String doctorID = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_DOCTOR_ID).getValue(String.class);
                    String appointmentID = child.child(Constants.FIREBASE_PATH_APPOINTMENT_ID).getValue(String.class);
                    Date date = child.child(Constants.FIREBASE_PATH_APPOINTMENTS_DATE).getValue(Date.class);
                    if (appointmentID.equals(thisAppointmentID)) {
                        System.out.println("Currently Looking at Appointment " + date);
                        thisAppointmentID = appointmentID;
                        thisAppointmentDate = date;
                        appointmentIDView.setText(appointmentID);
                        appointmentDateView.setText(date.toString());

                        ref.child(Constants.FIREBASE_PATH_USERS).addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    String childID = child.child(Constants.FIREBASE_PATH_USERS_ID).getValue(String.class);
                                    //Get Time of Now
                                    LocalDateTime timeNow = LocalDateTime.now();
                                    Date today = Date.from(timeNow.atZone(ZoneId.systemDefault()).toInstant());
                                    if (patientID.equals(childID)) {
                                        thisPatientId = patientID;
                                        thisPatientName = child.child(Constants.FIREBASE_PATH_USERS_FIRST_NAME).getValue(String.class) + " " + child.child(Constants.FIREBASE_PATH_USERS_LAST_NAME).getValue(String.class);
                                        thisPatientGender = child.child(Constants.FIREBASE_PATH_USERS_GENDER).getValue(String.class);
                                        patientGenderView.setText(thisPatientGender);
                                        patientNameView.setText(thisPatientName);
//                                        thisPatientBirthday = child.child(Constants.FIREBASE_PATH) // TODO: Birthday

                                    }else if(doctorID.equals(childID)){
                                        thisDoctorName = child.child(Constants.FIREBASE_PATH_USERS_FIRST_NAME).getValue(String.class) + " " + child.child(Constants.FIREBASE_PATH_USERS_LAST_NAME).getValue(String.class);
                                        doctorNameView.setText(thisDoctorName);
                                    }
                                }
//                                ArrayAdapter appointmentAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, appointments);
//                                appointmentsView.setAdapter(appointmentAdapter);
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
}