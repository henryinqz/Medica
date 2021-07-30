package com.example.medical_clinic_scheduling_app;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Doctor extends Person implements Observer {
    private Set<Appointment> upcomingAppointments;
    private Set<String> specializations;
    private Set<Patient> seenPatients;

//    Doctor(String username, String password, String firstname, String lastname, String gender, Date dateOfBirth, HashSet<String> specializations) {
//        super(username, password, firstname, lastname, gender, dateOfBirth);
//        upcomingAppointments = new TreeSet<Appointment>();
//        this.specializations = specializations;
//        seenPatients = new HashSet<Patient>();
//    }
    Doctor(String username, String firstname, String lastname) {
        super(username, firstname, lastname);
    }

    public void addSpecialization(String specialization) {
        specializations.add(specialization);
    }

    public void removeSpecialization(String specialization) {
        specializations.remove(specialization);
    }

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
