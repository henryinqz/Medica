package com.example.medical_clinic_scheduling_app.Activities.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_clinic_scheduling_app.R;

public class LoginViewActivity extends AppCompatActivity {
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        this.presenter = new LoginPresenter(new LoginModel(), this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginSubmit: // Submit button
//                loginUser();
                this.presenter.checkCredentials();
//                this.presenter.login();

                break;
        }
    }

    public void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    public void displayError(TextView textView, String errorMessage) {
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
}