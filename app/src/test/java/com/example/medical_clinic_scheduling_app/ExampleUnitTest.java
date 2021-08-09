package com.example.medical_clinic_scheduling_app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import com.example.medical_clinic_scheduling_app.Activities.Login.LoginModel;
import com.example.medical_clinic_scheduling_app.Activities.Login.LoginViewActivity;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    LoginModel model;

    @Mock
    LoginViewActivity view;

    @Test
    public void testPresenter() {

    }
}