package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Doctor extends Person {
    private List<String> specializations, upcomingAppointmentIDs, seenPatientIDs;

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
    // upcomingAppointmentIDs
    public List<String> getUpcomingAppointmentIDs() {
        return this.upcomingAppointmentIDs;
    }
    private void addUpcomingAppointment(Appointment upcomingAppt) {
        this.upcomingAppointmentIDs.add(upcomingAppt.getAppointmentID());
    }
    // seenPatientIDs
    public List<String> getSeenPatientIDs() {
        return this.seenPatientIDs;
    }

    private void addSeenPatient(Patient patient) { // TODO: Accessed by observers after appointment passes?
        this.seenPatientIDs.add(patient.getID());
    }
    public void updateBooking(String appt) {
        this.upcomingAppointmentIDs.add(appt);
    }

    public void updatePassing(Appointment appt) {
        this.upcomingAppointmentIDs.remove(appt.hashCode());
        this.seenPatientIDs.add(appt.getPatientID());
    }
}
