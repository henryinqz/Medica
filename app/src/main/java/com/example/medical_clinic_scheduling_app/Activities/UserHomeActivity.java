package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Appointment;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UserHomeActivity extends AppCompatActivity {
    private Person user;
    private final int HOUR_IN_MILLISECOND = 36000000;
    private static Calendar lastUpdated = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
//        if (lastUpdated == null || Calendar.getInstance().getTimeInMillis() - lastUpdated.getTimeInMillis() >= HOUR_IN_MILLISECOND){
//            lastUpdated=Calendar.getInstance();
            Appointment.expireAppointments();
//        }

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
                            txtWelcome1.setText(getResources().getString(R.string.user_home_welcome1) + " ");

                            if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) { // Doctor
                                btnTop.setText(getResources().getString(R.string.user_home_schedule)); // Set top button to "View/edit schedule"
                                txtWelcome1.append("Dr. ");
                            } else if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)) { // Patient
                                btnTop.setText(getResources().getString(R.string.user_home_book_appt));
                            }

                            txtWelcome1.append(user.toString());
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
//        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        switch (view.getId()) {
            case R.id.btnHomeBookOrSchedule: // Top button (Book (Patient) OR schedule (Doctor))
                if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) { // Doctor
                    Intent intent = new Intent(getApplicationContext(), DoctorViewAvailableTimeSlotsActivity.class);
                    startActivity(intent);
                } else if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)) { // Patient
                    Intent intent = new Intent(this, BookYourAppointmentMainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btnHomeUpcomingAppt: // View upcoming appointments
                if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)) { // Doctor
                    // Redirect to doctor page
                    Intent intent = new Intent(getApplicationContext(), DoctorViewAppointmentActivity.class);
                    startActivity(intent);
                } else if (user.getType().equals(Constants.PERSON_TYPE_PATIENT)) {// Patient
                    Intent intent = new Intent(getApplicationContext(), PatientAppointmentsViewActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btnHomePrevAppt: // View previous appointments
                Intent intent = new Intent(getApplicationContext(), ViewPreviousAppointmentsActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);

        MenuItem action_profile = menu.findItem(R.id.profile_icon);
        action_profile.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP); //

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_icon:
                startActivity(new Intent(getApplicationContext(), ProfileViewActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}