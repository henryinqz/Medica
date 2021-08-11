package com.example.medical_clinic_scheduling_app.Activities.Login;

import android.widget.TextView;

import com.example.medical_clinic_scheduling_app.R;

public class LoginPresenter implements LoginListener {
    protected LoginModel model;
    private LoginViewActivity view;

    public LoginPresenter(LoginViewActivity view) {
        this.view = view;
        this.model = new LoginModel(this);
    }

    public boolean checkCredentials() {
        String username = this.view.getUsername();
        String password = this.view.getPassword();
        if (username.isEmpty()) {
            this.view.displayError("Username cannot be empty", (TextView) this.view.findViewById(R.id.editTextLoginUsername));
            return false;
        } else if (password.isEmpty()) {
            this.view.displayError("Password cannot be empty", (TextView) this.view.findViewById(R.id.editTextLoginPassword));
            return false;
        }

        return true;
    }

    public void loginSuccess() {
        this.view.displayMessage("Logged in");
        this.view.startUserHomeActivity();
    }
    public void loginFailed(String errorMessage) {
        this.view.displayMessage("Error: " + errorMessage);
    }

}
