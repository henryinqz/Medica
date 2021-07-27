package com.example.medical_clinic_scheduling_app;

public class Date {
    private int year;
    private int month;
    private int day;
    private String weekday;

    Date(String weekday, int month, int day, int year) {
        this.weekday = weekday;
        this.month = month;
        this.day = day;
        this.year = year;
    }
}
