package com.example.medical_clinic_scheduling_app;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class BookYourAppointmentMain extends AppCompatActivity {

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
        final ArrayList<String> doctors = new ArrayList<String>();
        final ArrayAdapter doctorAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, doctors);
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
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String type = child.child("type").getValue(String.class);
                    String userGender = child.child("gender").getValue(String.class);
                    List<String> userSpecialization = (List<String>) child.child("specializations").getValue();

                    if (type.equals("DOCTOR") &&
                            (gender == null || (gender != null && (userGender.equals(gender) || gender.equals("Any Gender"))))
                            && (specialization == null ||
                            (specialization != null && userSpecialization != null
                                    && checkSpecializations(userSpecialization, specialization)))) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Dr. ");
                        stringBuilder.append(child.child("firstName").getValue(String.class));
                        stringBuilder.append(" ");
                        stringBuilder.append(child.child("lastName").getValue(String.class));
                        stringBuilder.append("\n");
                        stringBuilder.append(child.child("gender").getValue(String.class));
                        stringBuilder.append("\n");
                        Iterable<DataSnapshot> specializations = child.child("specializations").getChildren();
                        for (DataSnapshot specialist : specializations) {
                            stringBuilder.append(specialist.getValue(String.class));
                            stringBuilder.append("\n");
                        }
                        doctors.add(stringBuilder.toString());
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BookYourAppointmentMain.this, "Selected Doctors: " + doctors.get(i).toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SelectAppointmentTimes.class));
            }
        });
    }

    public void onFilterStartButtonClicked (View view){
        //TODO: filter database w/ selected options and return result to main page

        Intent intent = new Intent(this, AppointmentFilterOptions.class);
        startActivity(intent);
    }
}