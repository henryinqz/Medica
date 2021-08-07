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
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.DateUtility;
import com.example.medical_clinic_scheduling_app.Objects.Appointment;
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

public class ViewPreviousAppointmentsActivity extends AppCompatActivity {
    private boolean isDoctor = true; // Default, doesn't hurt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_previous_appointments);

        ArrayList<String> appointments = new ArrayList<>();
        ArrayList<String> appointmentIDs = new ArrayList<String>();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .child(userID)
                .child(Constants.FIREBASE_PATH_USERS_TYPE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userType = snapshot.getValue(String.class);
                        isDoctor = userType.equals(Constants.PERSON_TYPE_DOCTOR);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        ListView prevApptsView = (ListView) findViewById(R.id.List_of_Previous_Appointments);
        prevApptsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if (isDoctor) { // Only goes to details if doctor
                    Intent intent = new Intent(getApplicationContext(), DoctorViewAppointmentDetailsActivity.class);
                    intent.putExtra("Appointment", appointmentIDs.get(index)); // TODO: Change string name to appointmentID
                    startActivity(intent);
                }
            }
        });

        // Get prevAppointmentIDs from user
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .child(userID)
                .child(Constants.FIREBASE_PATH_USERS_APPTS_PREV)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot prevApptIDsSnapshot) {
                        for (DataSnapshot prevApptIDChild : prevApptIDsSnapshot.getChildren()) { // Loop through all of users's prevApptIDs
                            String prevApptID = prevApptIDChild.getValue(String.class);

                            // For each prevApptID, get the Appointment object
                            FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS)
                                    .child(prevApptID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot prevApptSnapshot) {
                                            // For each appointment, add the name of opposite user + date.toString() to prevApptsView
                                            Appointment appt = prevApptSnapshot.getValue(Appointment.class);
                                            String oppositeUserID;
                                            if (isDoctor) { // Opposite user is patient
                                                oppositeUserID = appt.getPatientID();
                                            } else { // Opposite user is doctor
                                                oppositeUserID = appt.getDoctorID();
                                            }

                                            FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                                    .child(oppositeUserID)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot oppositeUserSnapshot) {
                                                            Person user = oppositeUserSnapshot.getValue(Person.class);

                                                            // Generate string to add
                                                            String prevApptInfoToShow;
                                                            if (isDoctor)
                                                                prevApptInfoToShow = "Doctor: ";
                                                            else
                                                                prevApptInfoToShow = "Patient: ";
                                                            prevApptInfoToShow += user.toString() + "\n" + appt.getDate().toString();

                                                            appointments.add(prevApptInfoToShow);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) { // Error: Opposite user doesn't exist
                                                            // Generate string to add
                                                            String prevApptInfoToShow;
                                                            if (isDoctor)
                                                                prevApptInfoToShow = "Doctor: ";
                                                            else
                                                                prevApptInfoToShow = "Patient: ";
                                                            prevApptInfoToShow += "N/A" + "\n" + appt.getDate().toString();

                                                            appointments.add(prevApptInfoToShow);
                                                        }
                                                    });
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { // Error: appointment not found
                                            Toast.makeText(getApplicationContext(), "Error: Couldn't find appointment", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { // Error: no previous appointments
                        Toast.makeText(getApplicationContext(), "Error: No previous appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}