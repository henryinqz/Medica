package com.example.medical_clinic_scheduling_app.Activities.Login;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.medical_clinic_scheduling_app.Activities.UserHomeActivity;
import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.Objects.Person;
import com.example.medical_clinic_scheduling_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter {
    private LoginModel model;
    private LoginViewActivity view;

    public LoginPresenter(LoginModel model, LoginViewActivity view) {
        this.model = model;
        this.view = view;
    }

    public void checkCredentials() {
        String username = this.view.getUsername();
        String password = this.view.getPassword();
        if (username.isEmpty())
            this.view.displayError((TextView) this.view.findViewById(R.id.editTextLoginUsername), "Username cannot be empty");
        else if (password.isEmpty())
            this.view.displayError((TextView) this.view.findViewById(R.id.editTextLoginPassword), "Password cannot be empty");
        else
            login();

    }

    public void login() {
        String username = this.view.getUsername();
        String password = this.view.getPassword();

        String emailUsername = username + Constants.USERNAME_EMAIL_DOMAIN;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(emailUsername, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // Logged in
                    view.displayMessage("Logged in");

                    String userID = auth.getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(userID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Person user = snapshot.getValue(Person.class);
                                    if (user != null) {
                                        Intent intent = new Intent(view.getApplicationContext(), UserHomeActivity.class);
                                        intent.putExtra("userid", userID);
                                        view.startActivity(intent);
                                    } else { // user == null
                                        view.displayMessage("Error: User is empty");
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { // Failed to get user
                                    view.displayMessage("Error: Failed to get user");
                                }
                            });
                } else { // Failed to login
                    try {
                        switch(task.getException().getMessage()) {
                            case "There is no user record corresponding to this identifier. The user may have been deleted.":
                                view.displayError((TextView) view.findViewById(R.id.editTextLoginUsername), "Username does not exist");
                                break;
                            case "The password is invalid or the user does not have a password.":
                                view.displayError((TextView) view.findViewById(R.id.editTextLoginPassword), "Invalid password");
                                break;
                            case "A network error (such as timeout, interrupted connection or unreachable host) has occurred.":
                                view.displayMessage("Error: Could not connect to the server");
                                break;
                        }
                    } catch (NullPointerException e) {
                        view.displayMessage("Error: Failed to login");
                    }
                }
            }
        });
    }
}
