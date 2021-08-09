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

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    private final String EMPTY = "";
    private final String PASSWORD = "password";
    private final String USERNAME_DOCTOR = "doctor";
    private final String USERNAME_PATIENT = "patient";

    @Mock
    LoginModel model;

    @Mock
    LoginViewActivity view;

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
}