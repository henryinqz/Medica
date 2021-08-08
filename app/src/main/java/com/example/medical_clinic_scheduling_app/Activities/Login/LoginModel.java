package com.example.medical_clinic_scheduling_app.Activities.Login;

import androidx.annotation.NonNull;

import com.example.medical_clinic_scheduling_app.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginModel {
    private LoginListener loginListener;
    public LoginModel(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void login(String username, String password) {
        String emailUsername = username + Constants.USERNAME_EMAIL_DOMAIN;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(emailUsername, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // Logged in
                    loginListener.loginSuccess();
                } else { // Failed to login
                    try {
                        switch(task.getException().getMessage()) {
                            case "There is no user record corresponding to this identifier. The user may have been deleted.":
                                loginListener.loginFailed("Username does not exist");
                                break;
                            case "The password is invalid or the user does not have a password.":
                                loginListener.loginFailed("Invalid password");
                                break;
                            case "A network error (such as timeout, interrupted connection or unreachable host) has occurred.":
                                loginListener.loginFailed("Could not connect to the server");
                                break;
                        }
                    } catch (NullPointerException e) {
                        loginListener.loginFailed("Failed to login");
                    }
                }
            }
        });
    }
}
