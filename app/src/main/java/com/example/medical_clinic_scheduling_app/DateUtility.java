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

    public static int getCurrentDay(){
        return (Calendar.getInstance().get(Calendar.DATE));
    }

    public static int getCurrentMonth(){
        return (Calendar.getInstance().get(Calendar.MONTH));
    }

    public static int getCurrentYear(){
        return (Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String simpleDateFormater(int day, int month, int year, int hour, int minute, int second){
        //Strings are formatted in "yyyy/MM/dd HH:mm:ss"
        return year + "/" + month + "/" + day + "/" + " " + hour + ":" + minute + ":" + second;
    }
}

