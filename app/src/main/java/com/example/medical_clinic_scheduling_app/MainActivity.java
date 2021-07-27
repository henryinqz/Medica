package com.example.medical_clinic_scheduling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    // Test
    // Test (Justin)
    // Test (Brian)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Login button
    public void to_login(View view) {
        Intent intent = new Intent(this, LoginPageActivity.class);
        startActivity(intent);

    }

//    //Register button (Incomplete)
//    public void to_register(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        startActivity(intent);
//    }

}