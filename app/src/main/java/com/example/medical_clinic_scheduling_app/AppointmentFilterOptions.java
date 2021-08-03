package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class AppointmentFilterOptions extends AppCompatActivity {
    private HashSet<String> specializations = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_filter_options);

        //Setting up the Gender Dropdown List
        Spinner genderSpinner = (Spinner)findViewById(R.id.filterByGenderSpinner);
        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<>(AppointmentFilterOptions.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.FilterGender));
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderSpinnerAdapter);

        // Specialist dialog list
        Button btnSelectSpecialist = (Button) findViewById(R.id.filterSpecialistButton);
        btnSelectSpecialist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MultipleSpecialistSelectionFragment selectionDialog = new MultipleSpecialistSelectionFragment();
                selectionDialog.show(getSupportFragmentManager(), "SelectSpecialistDialog");
                specializations = selectionDialog.getSelectedItems();
            }
        });

    }
    public void onFilterButtonClicked (View view){
        //TODO: filter database w/ selected options and return result to MAIN APPOINTMENT page
        //Note: BookYourAppointmentMain is not the main appointment page.
        Intent intent = new Intent(this, BookYourAppointmentMain.class);
        Spinner genderSpinner = (Spinner) findViewById(R.id.filterByGenderSpinner);
        String gender = genderSpinner.getSelectedItem().toString();
        intent.putExtra("gender", gender);
        intent.putExtra("specialization", new ArrayList<String>(specializations));
        startActivity(intent);

    }
}