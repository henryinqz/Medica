package com.example.medical_clinic_scheduling_app.Activities.Login;

import android.widget.TextView;

import com.example.medical_clinic_scheduling_app.R;

public class LoginPresenter implements LoginListener {
    private LoginModel model;
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

    public void login() {
        String username = this.view.getUsername();
        String password = this.view.getPassword();
        this.model.login(username, password);
    }

    public void loginSuccess() {
        this.view.displayMessage("Logged in");
        this.view.startUserHomeActivity();
    }
    public void loginFailed(String errorMessage) {
        this.view.displayMessage("Error: " + errorMessage);
    }

//    public void login() {
//        String username = this.view.getUsername();
//        String password = this.view.getPassword();
//
//        String emailUsername = username + Constants.USERNAME_EMAIL_DOMAIN;
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.signInWithEmailAndPassword(emailUsername, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) { // Logged in
//                    view.displayMessage("Logged in");
//
//                    String userID = auth.getCurrentUser().getUid();
//                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
//                            .child(userID)
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    Person user = snapshot.getValue(Person.class);
//                                    if (user != null) {
//                                        Intent intent = new Intent(view.getApplicationContext(), UserHomeActivity.class);
//                                        intent.putExtra("userid", userID);
//                                        view.startActivity(intent);
//                                    } else { // user == null
//                                        view.displayMessage("Error: User is empty");
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) { // Failed to get user
//                                    view.displayMessage("Error: Failed to get user");
//                                }
//                            });
//                } else { // Failed to login
//                    try {
//                        switch(task.getException().getMessage()) {
//                            case "There is no user record corresponding to this identifier. The user may have been deleted.":
//                                view.displayError((TextView) view.findViewById(R.id.editTextLoginUsername), "Username does not exist");
//                                break;
//                            case "The password is invalid or the user does not have a password.":
//                                view.displayError((TextView) view.findViewById(R.id.editTextLoginPassword), "Invalid password");
//                                break;
//                            case "A network error (such as timeout, interrupted connection or unreachable host) has occurred.":
//                                view.displayMessage("Error: Could not connect to the server");
//                                break;
//                        }
//                    } catch (NullPointerException e) {
//                        view.displayMessage("Error: Failed to login");
//                    }
//                }
//            }
//        });
//    }
}
