package com.example.medical_clinic_scheduling_app;

import java.util.Calendar;
import java.util.Date;

public class DateUtility {
    public static boolean isSameDay(Date date1, Date date2) { // Compares Date objects and disregards time (ie. only checks if same year & day of year)
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}

