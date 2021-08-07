package com.example.medical_clinic_scheduling_app.Objects;

import com.example.medical_clinic_scheduling_app.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Patient extends Person {
    private Date dateOfBirth;
    private List<String> prevAppointmentIDs, upcomingAppointmentIDs, seenDoctorIDs;

    private Patient() {
        this.prevAppointmentIDs = new ArrayList<String>();
        this.upcomingAppointmentIDs = new ArrayList<String>();
        this.seenDoctorIDs = new ArrayList<String>();
    }
    public Patient(String username, String firstname, String lastname, String gender, Date dateOfBirth, String uid) {
        super(username, firstname, lastname, gender, Constants.PERSON_TYPE_PATIENT, uid);
        this.dateOfBirth = dateOfBirth;

        this.prevAppointmentIDs = new ArrayList<String>();
        this.upcomingAppointmentIDs = new ArrayList<String>();
        this.seenDoctorIDs = new ArrayList<String>();
    }

    // Getters/setters:
    // dateOfBirth
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    // prevAppointmentIDs
    public List<String> getPrevAppointmentIDs() {
        return this.prevAppointmentIDs;
    }
    public void setPrevAppointmentIDs(List<String> prevAppointmentIDs) {
        this.prevAppointmentIDs = prevAppointmentIDs;
    }
    public void addPrevAppointment(Appointment prevAppt) {
        this.prevAppointmentIDs.add(prevAppt.getAppointmentID());
    }
    // upcomingAppointmentIDs
    public List<String> getUpcomingAppointmentIDs() {
        return this.upcomingAppointmentIDs;
    }
    public void setUpcomingAppointmentIDs(List<String> upcomingAppointmentIDs) {
        this.upcomingAppointmentIDs = upcomingAppointmentIDs;
    }
    public void addUpcomingAppointment(Appointment upcomingAppt) {
        addUpcomingAppointment(upcomingAppt.getAppointmentID());
    }
    public void addUpcomingAppointment(String upcomingApptID) {
        this.upcomingAppointmentIDs.add(upcomingApptID);
    }
    public void removeUpcomingAppointment(Appointment upcomingAppt) {
        this.upcomingAppointmentIDs.remove(upcomingAppt.getAppointmentID());
    }
    // seenDoctorIDs
    public List<String> getSeenDoctorIDs() {
        return seenDoctorIDs;
    }
    public void setSeenDoctorIDs(List<String> seenDoctorIDs) {
        this.seenDoctorIDs = seenDoctorIDs;
    }
    public void addSeenDoctor(Doctor doctor) {
        this.seenDoctorIDs.add(doctor.getID());
    }
    public void addSeenDoctor(String doctorID) {
        this.seenDoctorIDs.add(doctorID);
    }
}
