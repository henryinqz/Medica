package com.example.medical_clinic_scheduling_app.Objects;

import com.example.medical_clinic_scheduling_app.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Doctor extends Person implements Serializable {
    private List<String> specializations, availableAppointmentIDs, prevAppointmentIDs, upcomingAppointmentIDs, seenPatientIDs;

    private Doctor() {
        this.specializations = new ArrayList<String>();
        this.availableAppointmentIDs = new ArrayList<String>();
        this.prevAppointmentIDs = new ArrayList<String>();
        this.upcomingAppointmentIDs = new ArrayList<String>();
        this.seenPatientIDs = new ArrayList<String>();
    }
    public Doctor(String username, String firstName, String lastName, String gender, HashSet<String> specializations, String uid) {
        super(username, firstName, lastName, gender, Constants.PERSON_TYPE_DOCTOR, uid);

        this.specializations = new ArrayList<String>(specializations);
        this.availableAppointmentIDs = new ArrayList<String>();
        this.prevAppointmentIDs = new ArrayList<String>();
        this.upcomingAppointmentIDs = new ArrayList<String>();
        this.seenPatientIDs = new ArrayList<String>();
        this.availableAppointmentIDs = new ArrayList<String>();
    }

    // Getters/setters:
    // specializations
    public List<String> getSpecializations() {
        return this.specializations;
    }
    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    // availableAppointmentIDs
    public List<String> getAvailableAppointmentIDs() {
        return availableAppointmentIDs;
    }
    public void setAvailableAppointmentIDs(List<String> availableAppointmentIDs) {
        this.availableAppointmentIDs = availableAppointmentIDs;
    }
    public void addAvailableAppointment(String availableApptID) {
        this.availableAppointmentIDs.add(availableApptID);
    }
    public void addAvailableAppointment(Appointment availableAppt) {
        this.addAvailableAppointment(availableAppt.getAppointmentID());
    }

    public void removeAvailableAppointment(String availableApptID) {
        this.availableAppointmentIDs.remove(availableApptID);
    }
    public void removeAvailableAppointment(Appointment availableAppt) {
        this.removeAvailableAppointment(availableAppt.getAppointmentID());
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
    public void addUpcomingAppointment(String upcomingApptID) {
        this.upcomingAppointmentIDs.add(upcomingApptID);
    }
    public void addUpcomingAppointment(Appointment upcomingAppt) {
        this.addUpcomingAppointment(upcomingAppt.getAppointmentID());
    }
    public void removeUpcomingAppointment(String upcomingApptID) {
        this.upcomingAppointmentIDs.remove(upcomingApptID);
    }
    public void removeUpcomingAppointment(Appointment upcomingAppt) {
        this.removeUpcomingAppointment(upcomingAppt.getAppointmentID());
    }
    // seenPatientIDs
    public List<String> getSeenPatientIDs() {
        return this.seenPatientIDs;
    }
    public void setSeenPatientIDs(List<String> seenPatientIDs) {
        this.seenPatientIDs = seenPatientIDs;
    }
    public void addSeenPatient(Patient patient) {
        this.seenPatientIDs.add(patient.getID());
    }
    public void addSeenPatient(String patientID) {
        this.seenPatientIDs.add(patientID);
    }


//    public void updateBooking(Appointment appt) {
//        this.removeAvailableAppointment(appt);
//        this.addUpcomingAppointment(appt);
//    }
//    public void updatePassing(Appointment appt) {
//        this.removeUpcomingAppointment(appt);
//        this.addSeenPatient(appt.getPatientID());
//    }

    @Override
    public String toString() {
        return "Dr. " + super.toString();
    }
}
