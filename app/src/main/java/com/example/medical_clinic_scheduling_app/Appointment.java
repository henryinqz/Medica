package com.example.medical_clinic_scheduling_app;

import java.util.Date;

public class Appointment implements Comparable<Appointment> {
    Date date;
    Doctor doctor;
    Patient patient;

    Appointment(Date date, Doctor doctor, Patient patient) {
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
    }

    @Override
    public int compareTo(Appointment o) {
        return this.date.compareTo(o.date);
    }
}
