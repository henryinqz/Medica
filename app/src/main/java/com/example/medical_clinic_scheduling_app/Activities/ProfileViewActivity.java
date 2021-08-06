package com.example.medical_clinic_scheduling_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileViewActivity extends AppCompatActivity {
    private Person user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        TextView profileName = findViewById(R.id.profileName);
        TextView profileGender = findViewById(R.id.profileGender);

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
                                    //TODO: show birthdate
                            } else if (user.getType().equals(Constants.PERSON_TYPE_DOCTOR)){
                                    //TODO: show specializations
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}