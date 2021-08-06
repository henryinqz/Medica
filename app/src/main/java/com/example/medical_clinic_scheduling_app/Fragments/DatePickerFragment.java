package com.example.medical_clinic_scheduling_app.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog datePickerDialog;
    private long minDate = -1, maxDate = -1;

    public DatePickerFragment() {
    }
    public DatePickerFragment(long minDate, long maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        this.datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);

        // If the respective parameter is -1, then no minDate/maxDate will be set
        if (minDate >= 0)
            setMinDate(minDate);
        else if (maxDate >= 0)
            setMaxDate(maxDate);

        return this.datePickerDialog;
    }

    private void setMinDate(long minDate) {
        this.datePickerDialog.getDatePicker().setMinDate(minDate); // Prevents user from choosing date in the future (ie. booking appointments)
    }
    private void setMaxDate(long maxDate) {
        this.datePickerDialog.getDatePicker().setMaxDate(maxDate); // Prevents user from choosing date in the future (ie. setting birthday)
    }
}
