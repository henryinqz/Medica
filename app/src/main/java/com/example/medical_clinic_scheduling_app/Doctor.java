package com.example.medical_clinic_scheduling_app;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Doctor extends Person implements Observer {
    private Set<Appointment> upcomingAppointments;
//    private Set<String> specializations;
    private String specialization;
    private Set<Patient> seenPatients;

    private Doctor(){
    }

    Doctor(String username, String firstName, String lastName, String gender, HashSet<String> specializations) {
//    Doctor(String username, String firstName, String lastName, String gender, String specialization) {
        super(username, firstName, lastName, gender, Constants.PERSON_TYPE_DOCTOR);
        this.specialization = specialization; // TODO: Replace with HashSet (ie. able to have multiple specializations)
        this.upcomingAppointments = new TreeSet<Appointment>();
        this.seenPatients = new HashSet<Patient>();
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
        seenPatients.add(patient);
    }


    @Override
    public void updateBooking(Appointment appointment) {
        upcomingAppointments.add(appointment);
    }

    @Override
    public void updatePassing(Appointment appointment) {
        upcomingAppointments.remove(appointment);
        seenPatients.add(appointment.patient);
    }
}
