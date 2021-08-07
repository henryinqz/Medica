package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Doctor;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookYourAppointmentMainActivity extends AppCompatActivity {

    protected boolean checkSpecializations (List<String> a, List<String> b){
        for (String s: b){
            if (!a.contains(s)){
                return false;
            }
        }
        return true;
    }

    private Doctor selectedDoctor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_your_appointment_main);

        //Setting up ListView of Doctors
        ListView doctorView = (ListView) findViewById(R.id.doctorListView);
        final ArrayList<String> printDoctors = new ArrayList<String>();
        final ArrayList<Person> doctors = new ArrayList<Person>();
        final ArrayAdapter doctorAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, printDoctors);
        //The format can be: "Dr. NAME\nGENDER\nSPECIALIZATION"
//        doctors.add("Dr. Eric Zhou\nMale\nCardiology");

        //Getting filter options
        String gender = getIntent().getStringExtra("gender"); //null if DNE
        ArrayList<String> specialization = getIntent().getStringArrayListExtra("specialization");

        //Updating filter textView
        String genderText;
        if (gender == null){
            genderText = "";
        } else { genderText=gender; }
        StringBuilder specializationText = new StringBuilder();
        if (specialization == null) { specializationText.append(""); }
        else {
            for (String s : specialization) {
                specializationText.append(s + "\n");
            }

        }
        TextView filterOptions = (TextView) findViewById(R.id.filterOptionsSelected);
        filterOptions.setText(genderText + "\n" + specializationText.toString());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(Constants.FIREBASE_PATH_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String type = child.child(Constants.FIREBASE_PATH_USERS_TYPE).getValue(String.class);
                    String userGender = child.child(Constants.FIREBASE_PATH_USERS_GENDER).getValue(String.class);
                    List<String> userSpecialization = (List<String>) child.child(Constants.FIREBASE_PATH_DOCTORS_SPECIALIZATIONS).getValue();

                    if (type.equals(Constants.PERSON_TYPE_DOCTOR) &&
                            (gender == null || (gender != null && (userGender.equals(gender) || gender.equals("Any Gender"))))
                            && (specialization == null ||
                            (specialization != null && userSpecialization != null
                                    && checkSpecializations(userSpecialization, specialization)))) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Dr. ");
                        stringBuilder.append(child.child(Constants.FIREBASE_PATH_USERS_FIRST_NAME).getValue(String.class));
                        stringBuilder.append(" ");
                        stringBuilder.append(child.child(Constants.FIREBASE_PATH_USERS_LAST_NAME).getValue(String.class));
                        stringBuilder.append("\n");
                        stringBuilder.append(child.child(Constants.FIREBASE_PATH_USERS_GENDER).getValue(String.class));
                        stringBuilder.append("\n");
                        Iterable<DataSnapshot> specializations = child.child(Constants.FIREBASE_PATH_DOCTORS_SPECIALIZATIONS).getChildren();
                        for (DataSnapshot specialist : specializations) {
                            stringBuilder.append(specialist.getValue(String.class));
                            stringBuilder.append("\n");
                        }
                        printDoctors.add(stringBuilder.toString());
                        doctors.add(child.getValue(Person.class));
                    }
                }
                doctorView.setAdapter(doctorAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        doctorView.setAdapter(doctorAdapter);

        //Setting up listener for when item is clicked.
        doctorView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Toast.makeText(BookYourAppointmentMainActivity.this, "Selected Dr. " + doctors.get(index).toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SelectAppointmentTimesActivity.class);
                intent.putExtra("doctor", printDoctors.get(index).toString());
                intent.putExtra("doctorID", doctors.get(index).getID());
                startActivity(intent);
            }
        });
    }

    public void onFilterStartButtonClicked (View view){
        //TODO: filter database w/ selected options and return result to main page

        Intent intent = new Intent(this, AppointmentFilterOptionsActivity.class);
        startActivity(intent);
    }
}