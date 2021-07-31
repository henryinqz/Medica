package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMainLogin: // Login button
                startActivity(new Intent(this, LoginPageActivity.class));
                break;
            case R.id.btnMainRegister: // Register button
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    /* // Login button
    public void to_login(View view) {
        Intent intent = new Intent(this, LoginPageActivity.class);
        startActivity(intent);
    }

    //Register button
    public void to_register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    } */
}