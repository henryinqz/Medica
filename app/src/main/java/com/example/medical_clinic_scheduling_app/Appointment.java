package com.example.medical_clinic_scheduling_app;

import java.util.Date;

public class Appointment implements Comparable<Appointment> {
    private Date date;
    private String doctorID, patientID, appointmentID;

    Appointment(Date date, Doctor doctor, Patient patient) {
        this.date = date;
        this.doctorID = doctor.getID();
        this.patientID = patient.getID();

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

    @Override
    public int compareTo(Appointment o) {
        return this.date.compareTo(o.date);
    }
}
