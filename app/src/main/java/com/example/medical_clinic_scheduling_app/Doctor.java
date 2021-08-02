package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Doctor extends Person implements Observer {
    private List<Integer> upcomingAppointments;
//    private List<String> specializations;
    private String specialization;
    private List<Integer> seenPatients;

    Doctor(){

    }

    //Doctor(String username, String firstName, String lastName, String gender, HashSet<String> specializations) {
    Doctor(String username, String firstName, String lastName, String gender, String specialization) {
        super(username, firstName, lastName, gender, Constants.PERSON_TYPE_DOCTOR);
        this.specialization = specialization; // TODO: Replace with HashSet (ie. able to have multiple specializations)
        this.upcomingAppointments = new ArrayList<Integer>();
        this.seenPatients = new ArrayList<Integer>();
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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

    // TODO: Uncomment after specializations (ie. HashSet) is reimplemented
//    public void addSpecialization(String specialization) {
//        specializations.add(specialization);
//    }
//
//    public void removeSpecialization(String specialization) {
//        specializations.remove(specialization);
//    }

    public void addSeenPatient(Patient patient) {
        seenPatients.add(patient.hashCode());
    }


    @Override
    public void updateBooking(Appointment appointment) {
        upcomingAppointments.add(appointment.hashCode());
    }

    @Override
    public void updatePassing(Appointment appointment) {
        upcomingAppointments.remove(appointment.hashCode());
        seenPatients.add(appointment.patient.hashCode());
    }
}
