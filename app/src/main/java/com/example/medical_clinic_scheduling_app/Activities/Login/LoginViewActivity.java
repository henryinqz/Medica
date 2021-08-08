package com.example.medical_clinic_scheduling_app.Activities.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.Activities.UserHomeActivity;
import com.example.medical_clinic_scheduling_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewActivity extends AppCompatActivity {
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        this.presenter = new LoginPresenter(this); // LoginModel is defined in LoginPresenter constructor so the LoginModel can listen to this.presenter (which implements LoginListener)
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginSubmit: // Submit button
                this.presenter.checkCredentials();
                break;
        }
    }

    public void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    public void displayError(String errorMessage, TextView textView) {
        textView.setError(errorMessage);
    }

    public String getUsername() {
        EditText usernameEditText = (EditText) findViewById(R.id.editTextLoginUsername);
        String username = usernameEditText.getText().toString().trim();
        return username;
    }
    public String getPassword() {
        EditText passwordEditText = (EditText) findViewById(R.id.editTextLoginPassword);
        String password = passwordEditText.getText().toString().trim();
        return password;
    }

    public void startUserHomeActivity() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) { // Go to user home
            Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
            startActivity(intent);
        } else {
            displayMessage("Error: User is not logged in");
        }
    }
}