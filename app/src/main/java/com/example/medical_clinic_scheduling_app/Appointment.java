package com.example.medical_clinic_scheduling_app;

import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return date.equals(that.date) && doctor.equals(that.doctor) && patient.equals(that.patient);
    }
    @Override
    public int hashCode() {
        return Objects.hash(date, doctor, patient);
    }
}
