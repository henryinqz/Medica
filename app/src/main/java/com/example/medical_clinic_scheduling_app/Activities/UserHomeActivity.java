package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Doctor;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserHomeActivity extends AppCompatActivity {
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Get logged in user info
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Person.class);
                        if (user != null) {
                            Button btnTop = (Button) findViewById(R.id.btnHomeBookOrSchedule);
                            TextView txtWelcome1 = (TextView) findViewById(R.id.txtHomeWelcome1);
                            txtWelcome1.setText(getResources().getString(R.string.user_home_welcome1) + " " + user.toString());

                            if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) { // Doctor
                                btnTop.setText(getResources().getString(R.string.user_home_schedule)); // Set top button to "View/edit schedule"
                            } else if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)) { // Patient
                                btnTop.setText(getResources().getString(R.string.user_home_book_appt));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: user is null", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error: user does not exist", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHomeBookOrSchedule: // Top button (Book (Patient) OR schedule (Doctor))
                if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) { // Doctor
                    // TODO: Start Doctor scheduling activity
                } else if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)) {// Patient
                    // TODO: Start Patient book appointment
                    Intent intent = new Intent(this, BookYourAppointmentMainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btnHomeUpcomingAppt: // View upcoming appointments
                if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) { // Doctor
                    // Redirect to doctor page
                    Intent intent = new Intent(getApplicationContext(), DoctorViewAppointmentActivity.class);
                    intent.putExtra("userid", getIntent().getStringExtra("userid"));
                    startActivity(intent);
                } else if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)) {// Patient
                    // Redirect to patient page
                    Intent intent = new Intent(getApplicationContext(), PatientAppointmentsViewActivity.class);
                    intent.putExtra("userid", getIntent().getStringExtra("userid"));
                    startActivity(intent);
                }
                break;
            case R.id.btnHomePrevAppt: // View previous appointments
                if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) { // Doctor
                    // TODO: Start Doctor view prev appts
                } else if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)) {// Patient
                    // TODO: Start Patient view prev appts
                }
                break;
        }
    }
}