package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Doctor extends Person implements Observer {
    private List<String> specializations;
    private List<Integer> upcomingAppointmentIDs, seenPatientIDs;

    Doctor(String username, String firstName, String lastName, String gender, HashSet<String> specializations) {
        super(username, firstName, lastName, gender, Constants.PERSON_TYPE_DOCTOR);

        this.specializations = new ArrayList<String>(specializations);
        this.upcomingAppointmentIDs = new ArrayList<Integer>();
        this.seenPatientIDs = new ArrayList<Integer>();
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
    public List<Integer> getUpcomingAppointmentIDs() {
        return this.upcomingAppointmentIDs;
    }
    public void addUpcomingAppointment(Appointment upcomingAppt) {
        this.upcomingAppointmentIDs.add(upcomingAppt.hashCode());
    }
    // seenPatientIDs
    public List<Integer> getSeenPatientIDs() {
        return this.seenPatientIDs;
    }
    public void addSeenPatient(Patient patient) { // TODO: Maybe private & only accessed by observers after appointment passes?
        this.seenPatientIDs.add(patient.hashCode());
    }


    @Override
    public void updateBooking(Appointment appt) {
        this.upcomingAppointmentIDs.add(appt.hashCode());
    }
    @Override
    public void updatePassing(Appointment appt) {
        this.upcomingAppointmentIDs.remove(appt.hashCode());
        this.seenPatientIDs.add(appt.patient.hashCode());
    }
}
