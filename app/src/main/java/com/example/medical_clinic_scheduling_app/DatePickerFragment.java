package com.example.medical_clinic_scheduling_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // Prevents user from choosing date in the future (ie. setting birthday)
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Prevents user from choosing date in the past (ie. booking appointments

        return datePickerDialog;
    }
}
