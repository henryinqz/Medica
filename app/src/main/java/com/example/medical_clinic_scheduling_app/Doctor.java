package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Doctor extends Person implements Observer {
    private List<String> specializations;
    private List<Integer> upcomingAppointmentIDs;
    private List<Integer> seenPatientIDs;

    Doctor(String username, String firstName, String lastName, String gender, HashSet<String> specializations) {
        super(username, firstName, lastName, gender, Constants.PERSON_TYPE_DOCTOR);

        this.specializations = new ArrayList<String>(specializations);
        this.upcomingAppointmentIDs = new ArrayList<Integer>();
        this.seenPatientIDs = new ArrayList<Integer>();
    }

    public List<String> getSpecializations() {
        return specializations;
    }
    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    // TODO: Fix crashes when these Set<> getters/setters are uncommented
//    public Set<Patient> getSeenPatients() {
//        return seenPatients;
//    }
//
//    public void setSeenPatients(Set<Patient> seenPatients) {
//        this.seenPatients = seenPatients;
//    }
//
//    public Set<Appointment> getUpcomingAppointments() {
//        return upcomingAppointments;
//    }
//
//    public void setUpcomingAppointments(Set<Appointment> upcomingAppointments) {
//        this.upcomingAppointments = upcomingAppointments;
//    }

    public void addSeenPatient(Patient patient) {
        seenPatientIDs.add(patient.hashCode());
    }


    @Override
    public void updateBooking(Appointment appointment) {
        upcomingAppointmentIDs.add(appointment.hashCode());
    }

    @Override
    public void updatePassing(Appointment appointment) {
        upcomingAppointmentIDs.remove(appointment.hashCode());
        seenPatientIDs.add(appointment.patient.hashCode());
    }
}
