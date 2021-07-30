package com.example.medical_clinic_scheduling_app;

import java.util.Set;

public class Doctor extends Person implements Observer {
    private Set<Appointment> upcomingAppointments;
    private Set<String> specializations;
    private Set<Patient> seenPatients;

//    Doctor(String username, String firstname, String lastname, String gender, HashSet<String> specializations) {
//        super(username, firstname, lastname, gender);
//        this.upcomingAppointments = new TreeSet<Appointment>();
//        this.specializations = specializations;
//        this.seenPatients = new HashSet<Patient>();
//    }
    Doctor(String username, String firstname, String lastname) { //
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
