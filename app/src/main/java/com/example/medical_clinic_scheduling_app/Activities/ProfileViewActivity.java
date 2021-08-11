package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Appointment;
import com.example.medical_clinic_scheduling_app.Objects.Doctor;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileViewActivity extends AppCompatActivity {
    private Person user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        hideLeaveClinicButtonVisibility(true); // Hide while getting if user is doctor or not

        TextView profileName = (TextView) findViewById(R.id.profileName);
        TextView profileUsername = (TextView) findViewById(R.id.profileUsername);
        TextView profileGender = (TextView) findViewById(R.id.profileGender);
        TextView profileBdaySpecializations = (TextView) findViewById(R.id.profileBdaySpecializations);
        TextView profileBdaySpecializationsTitle = (TextView) findViewById(R.id.profileBdaySpecializationsTitle);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Person.class);
                        if (user != null) {
                            profileName.setText(user.toString());
                            profileUsername.setText(user.getUsername());
                            profileGender.setText(user.getGender());
                            if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)){
                                String pattern = "yyyy-MM-dd";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                String birthDate = dateFormat.format(
                                        snapshot.child(Constants.FIREBASE_PATH_USERS_DATE_OF_BIRTH).getValue(Date.class));
                                profileBdaySpecializations.setText(birthDate);
                            } else if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)){
                                profileBdaySpecializationsTitle.setText(getResources().getString(R.string.profile_specializations));

                                List<String> specializations = (List<String>) snapshot.child(Constants.FIREBASE_PATH_DOCTORS_SPECIALIZATIONS).getValue();
                                StringBuilder specText = new StringBuilder();
                                if (specializations != null) {
                                    for (String specialization : specializations) {
                                        specText.append("- " + specialization + "\n");
                                    }
                                    profileBdaySpecializations.setText(specText.toString());
                                }
                                hideLeaveClinicButtonVisibility(false); // Unhide leave clinic for Doctor
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }


    public void onClick(View view){
        switch (view.getId()) {
            case R.id.logoutButton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.leaveClinic:
                if (this.user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want to leave the Medica clinic?\n\nYour account will be deleted and cannot be recovered.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    leaveClinic();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
        }
    }

    private void hideLeaveClinicButtonVisibility(boolean hidden) {
        Button leaveClinicBtn = (Button) findViewById(R.id.leaveClinic);
        if (hidden) // Hide
            leaveClinicBtn.setVisibility(View.GONE);
        else // Unhide
            leaveClinicBtn.setVisibility(View.VISIBLE);
    }

    private void leaveClinic() { // Delete doctor from clinic
        if (this.user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) {
            // TODO: Potentially show dialog to enter password & reauthenticate?
            String doctorID = this.user.getID();
            DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS).child(this.user.getID());
            doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot doctorSnapshot) {
                    // Get all doctor's availableAppointmentIDs & delete them from Firebase
                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(doctorID)
                            .child(Constants.FIREBASE_PATH_USERS_APPTS_AVAILABLE)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot doctorAvailableApptIDsSnapshot) {
                                    for (DataSnapshot doctorAvailableApptIDChild : doctorAvailableApptIDsSnapshot.getChildren()) {
                                        String availableApptID = doctorAvailableApptIDChild.getValue(String.class);
                                        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS).child(availableApptID).removeValue();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                    // Delete upcomingAppointmentIDs from database & from patient upcomingAppointmentIDs
                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(doctorID)
                            .child(Constants.FIREBASE_PATH_USERS_APPTS_UPCOMING)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot doctorUpcomingApptIDsSnapshot) {
                                    for (DataSnapshot doctorUpcomingApptIDChild : doctorUpcomingApptIDsSnapshot.getChildren()) {
                                        String upcomingApptID = doctorUpcomingApptIDChild.getValue(String.class);

                                        DatabaseReference upcomingApptRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS).child(upcomingApptID);
                                        upcomingApptRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot upcomingApptSnapshot) {
                                                Appointment upcomingAppt = upcomingApptSnapshot.getValue(Appointment.class);

                                                // Delete from patient upcoming appt ID
                                                FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                                        .child(upcomingAppt.getPatientID())
                                                        .child(Constants.FIREBASE_PATH_USERS_APPTS_UPCOMING)
                                                        .child(upcomingApptID)
                                                        .removeValue();

                                                upcomingApptRef.removeValue(); // Delete from Firebase
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                    FirebaseAuth.getInstance().getCurrentUser().delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    doctorRef.removeValue(); // Delete User > doctor

                                    Toast.makeText(getApplicationContext(), "Successfully deleted account", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { // Needs reauthentication
                            Toast.makeText(getApplicationContext(), "Error: Please logout and try again", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}