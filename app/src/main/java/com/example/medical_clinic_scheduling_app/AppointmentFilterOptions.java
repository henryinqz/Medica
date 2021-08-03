package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointmentFilterOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_filter_options);

        //Setting up the Gender Dropdown List
        Spinner genderSpinner = (Spinner)findViewById(R.id.filterByGenderSpinner);
        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<>(AppointmentFilterOptions.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.SelectGender));
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderSpinner.setAdapter(genderSpinnerAdapter);

        //Setting up the Specialist Dropdown List
        Spinner specialistSpinner = (Spinner)findViewById(R.id.filterBySpecializationSpinner);
        ArrayAdapter<String> specialistSpinnerAdapter = new ArrayAdapter<>(AppointmentFilterOptions.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.SelectSpecialist));
        specialistSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialistSpinner.setAdapter(specialistSpinnerAdapter);
    }
    public void onFilterButtonClicked (View view){
        //TODO: filter database w/ selected options and return result to MAIN APPOINTMENT page
        //Note: BookYourAppointmentMain is not the main appointment page.
        Intent intent = new Intent(this, BookYourAppointmentMain.class);
        Spinner genderSpinner = (Spinner) findViewById(R.id.filterByGenderSpinner);
        Spinner specializationSpinner = (Spinner) findViewById(R.id.filterBySpecializationSpinner);
        String gender = genderSpinner.getSelectedItem().toString();
        String specialization = specializationSpinner.getSelectedItem().toString();
        intent.putExtra("gender", gender);
        intent.putExtra("specialization", specialization);
        startActivity(intent);

    }
}