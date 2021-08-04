package com.example.medical_clinic_scheduling_app;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class Appointment implements Comparable<Appointment> {
    private Date date;
    private String doctorID, patientID, appointmentID;

    private Appointment() {

    }
    Appointment(Date date, Doctor doctor, Patient patient) {
        this.date = date;
        this.doctorID = doctor.getID();

        if (patient == null) { // i.e. appointment is available
            this.setPatientID("");
        } else {
            this.setPatientID(patient.getID());
        }

        this.appointmentID = hashCode() + ""; // TODO: Make a better ID?
    }

    // Getters/setters:
    // Date
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    // doctorID
    public String getDoctorID() {
        return this.doctorID;
    }
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    // patientID
    public String getPatientID() {
        return this.patientID;
    }
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    // appointmentID
    public String getAppointmentID() {
        return this.appointmentID;
    }
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public boolean isBooked() {
        return !this.patientID.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return getDate().equals(that.getDate()) && getDoctorID().equals(that.getDoctorID()) && getPatientID().equals(that.getPatientID()) && getAppointmentID().equals(that.getAppointmentID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getDoctorID(), getPatientID());
    }

    @Override
    public int compareTo(Appointment o) {
        return this.date.compareTo(o.date);
    }
}