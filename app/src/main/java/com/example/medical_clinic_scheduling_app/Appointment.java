package com.example.medical_clinic_scheduling_app;

public class Appointment {
    Date date;
    Doctor doctor;
    Patient patient;

    Appointment(Date date, Doctor doctor, Patient patient) {
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
    }
}
