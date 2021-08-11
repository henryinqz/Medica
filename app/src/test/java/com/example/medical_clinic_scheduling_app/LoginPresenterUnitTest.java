package com.example.medical_clinic_scheduling_app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.widget.TextView;

import com.example.medical_clinic_scheduling_app.Activities.Login.LoginModel;
import com.example.medical_clinic_scheduling_app.Activities.Login.LoginPresenter;
import com.example.medical_clinic_scheduling_app.Activities.Login.LoginViewActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterUnitTest {
    private final String EMPTY = "";
    private final String PASSWORD = "password";
    private final String USERNAME_DOCTOR = "doctor";
    private final String USERNAME_PATIENT = "patient";

    @Mock
    LoginModel model;

    @Mock
    LoginViewActivity view;

    // checkCredentials()
    @Test
    public void testPresenterCheckCredentialsEmptyUsername() {
        when(view.getUsername()).thenReturn(EMPTY);
        LoginPresenter presenter = new LoginPresenter(view);

        presenter.checkCredentials();
        verify(view).displayError("Username cannot be empty", (TextView) view.findViewById(R.id.editTextLoginUsername));
    }
    @Test
    public void testPresenterCheckCredentialsEmptyPassword() {
        when(view.getUsername()).thenReturn("nonempty");
        when(view.getPassword()).thenReturn(EMPTY);
        LoginPresenter presenter = new LoginPresenter(view);

        presenter.checkCredentials();
        verify(view).displayError("Password cannot be empty", (TextView) view.findViewById(R.id.editTextLoginPassword));
    }
    @Test
    public void testPresenterCheckCredentialsSuccessful() {
        when(view.getUsername()).thenReturn(USERNAME_PATIENT);
        when(view.getPassword()).thenReturn(PASSWORD);
        LoginPresenter presenter = new LoginPresenter(view);

        assertTrue(presenter.checkCredentials());
    }

    // login()
//    @Test
//    public void testPresenterLogin() {
//        when(view.getUsername()).thenReturn(USERNAME_PATIENT);
//        when(view.getPassword()).thenReturn(PASSWORD);
//        String username = view.getUsername();
//        String password = view.getPassword();
//        LoginPresenter presenter = new LoginPresenter(view);
//
//        presenter.login();
//        verify(model).login(username, password);
//
//    }

    // loginSuccess()
    @Test
    public void testPresenterLoginSuccess() {
        LoginPresenter presenter = new LoginPresenter(view);

        presenter.loginSuccess();
        verify(view).displayMessage("Logged in");
        verify(view).startUserHomeActivity();
    }

    // loginFailed()
    @Test
    public void testPresenterLoginFailed1() {
        LoginPresenter presenter = new LoginPresenter(view);
        String errorMessage = "There is no user record corresponding to this identifier. The user may have been deleted.";

        presenter.loginFailed(errorMessage);
        verify(view).displayMessage("Error: " + errorMessage);
    }
    @Test
    public void testPresenterLoginFailed2() {
        LoginPresenter presenter = new LoginPresenter(view);
        String errorMessage = "The password is invalid or the user does not have a password.";

        presenter.loginFailed(errorMessage);
        verify(view).displayMessage("Error: " + errorMessage);
    }
    @Test
    public void testPresenterLoginFailed3() {
        LoginPresenter presenter = new LoginPresenter(view);
        String errorMessage = "A network error (such as timeout, interrupted connection or unreachable host) has occurred.";

        presenter.loginFailed(errorMessage);
        verify(view).displayMessage("Error: " + errorMessage);
    }

}