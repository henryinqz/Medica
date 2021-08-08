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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfileViewActivity extends AppCompatActivity {
    private Person user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        hideLeaveClinicButtonVisibility(true); // Hide while getting if user is doctor or not

        TextView profileName = findViewById(R.id.profileName);
        TextView profileGender = findViewById(R.id.profileGender);
        TextView profileBDorSpec = findViewById(R.id.profileBDorSpec);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Person.class);
                        if (user != null) {
                            profileName.setText(user.toString());
                            profileGender.setText(user.getGender());
                            if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)){
                                String pattern = "yyyy-MM-dd";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                String birthDate = dateFormat.format(
                                        snapshot.child(Constants.FIREBASE_PATH_USERS_DATE_OF_BIRTH).getValue(Date.class));
                                profileBDorSpec.setText(birthDate);
                            } else if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)){
                                List<String> specializations = (List<String>) snapshot.child(Constants.FIREBASE_PATH_DOCTORS_SPECIALIZATIONS).getValue();
                                StringBuilder specText = new StringBuilder();
                                if (specializations != null) {
                                    for (String specialization : specializations) {
                                        specText.append(specialization + "\n");
                                    }
                                    profileBDorSpec.setText(specText.toString());
                                }

                                hideLeaveClinicButtonVisibility(false); // Unhide logout
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
            // TODO: Show dialog to enter password & reauthenticate.
            // TODO: On complete, delete all upcomingAppointments, delete from Users > userID, then delete user from FirebaseAuth

            FirebaseAuth.getInstance().getCurrentUser().delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Successfully deleted account", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}