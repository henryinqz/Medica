package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.DateUtility;
import com.example.medical_clinic_scheduling_app.Objects.Appointment;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorViewAvailableTimeSlotsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_available_time_slots);

        ListView availableTimesView = (ListView) findViewById(R.id.List_of_Available_Time_Slots);
        ArrayList<String> availableApptTimes = new ArrayList<String>();
        ArrayAdapter availableTimesAdaptor = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, availableApptTimes);

        String doctorID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Access Users > doctorID > availableAppointmentIDs
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .child(doctorID)
                .child(Constants.FIREBASE_PATH_USERS_APPTS_AVAILABLE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot availableApptIDsSnapshot) {
                        for (DataSnapshot child : availableApptIDsSnapshot.getChildren()) { // Each upcomingAppointmentID of the doctor
                            String availableAppointmentID = child.getValue(String.class);

                            // For each availableAppointmentID, get the appointmentID
                            FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS)
                                    .child(availableAppointmentID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot apptSnapshot) {
                                            // For each appointment, add the date.toString() to availableTimes
                                            Appointment appt = apptSnapshot.getValue(Appointment.class);
                                            if (!appt.isBooked()) { // Ensure available
                                                availableApptTimes.add(appt.getDate().toString());
                                            }
                                            ArrayAdapter availableTimesAdaptor = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, availableApptTimes);
                                            availableTimesView.setAdapter(availableTimesAdaptor);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { // Error: appointment not found
                                            Toast.makeText(getApplicationContext(), "Error: Couldn't find appointment", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { // Error: no available appointments
                        Toast.makeText(getApplicationContext(), "Error: No available appointments", Toast.LENGTH_SHORT).show();
                    }
                });

//        ArrayAdapter availableTimesAdaptor = new ArrayAdapter(this, android.R.layout.simple_list_item_1, availableApptTimes);
//        availableTimesView.setAdapter(availableTimesAdaptor);

//        availableTimesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Intent intent = new Intent(getApplicationContext(), DoctorViewAppointmentDetailsActivity.class);
////                intent.putExtra("Appointment", availableTimes.get(i));
////                startActivity(intent);
//            }
//        });
    }
}