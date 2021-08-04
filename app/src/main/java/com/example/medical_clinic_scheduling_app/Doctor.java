package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Doctor extends Person {
    private List<String> specializations, availableAppointmentIDs, upcomingAppointmentIDs, seenPatientIDs;

    Doctor(String username, String firstName, String lastName, String gender, HashSet<String> specializations, String uid) {
        super(username, firstName, lastName, gender, Constants.PERSON_TYPE_DOCTOR, uid);

        this.specializations = new ArrayList<String>(specializations);
        this.upcomingAppointmentIDs = new ArrayList<String>();
        this.seenPatientIDs = new ArrayList<String>();
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
    public void addAvailableAppointment(Appointment availableAppt) {
        this.availableAppointmentIDs.add(availableAppt.getAppointmentID());
    }
    private void removeAvailableAppointment(Appointment availableAppt) {
        this.availableAppointmentIDs.remove(availableAppt.getAppointmentID());
    }
    // upcomingAppointmentIDs
    public List<String> getUpcomingAppointmentIDs() {
        return this.upcomingAppointmentIDs;
    }
    private void addUpcomingAppointment(Appointment upcomingAppt) {
        this.upcomingAppointmentIDs.add(upcomingAppt.getAppointmentID());
    }
    private void removeUpcomingAppointment(Appointment upcomingAppt) {
        this.upcomingAppointmentIDs.remove(upcomingAppt.getAppointmentID());
    }
    // seenPatientIDs
    public List<String> getSeenPatientIDs() {
        return this.seenPatientIDs;
    }
    private void addSeenPatient(Patient patient) {
        this.seenPatientIDs.add(patient.getID());
    }
    private void addSeenPatient(String patientID) {
        this.seenPatientIDs.add(patientID);
    }


    public void updateBooking(Appointment appt) {
        this.removeAvailableAppointment(appt);
        this.addUpcomingAppointment(appt);
    }
    public void updatePassing(Appointment appt) {
        this.removeUpcomingAppointment(appt);
        this.addSeenPatient(appt.getPatientID());
    }
}
