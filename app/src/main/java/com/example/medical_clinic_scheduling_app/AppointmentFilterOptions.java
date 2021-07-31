package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AppointmentFilterOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_filter);

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
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialistSpinner.setAdapter(specialistSpinnerAdapter);
    }
    public void onFilterButtonClicked (View view){
        //TODO: filter database w/ selected options and return result to main page

        Intent intent = new Intent(this, BookYourAppointmentMain.class);
        startActivity(intent);
    }
}