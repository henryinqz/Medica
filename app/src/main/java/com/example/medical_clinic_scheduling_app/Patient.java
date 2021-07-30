package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Patient extends Person implements Subject {
    private Date dateOfBirth;

    private Set<Appointment> previousAppointments;
    private Set<Appointment> upcomingAppointments;
    private Set<Doctor> seenDoctors;
    private List<Observer> observers;

//    Patient(String username, String firstname, String lastname, String gender, Date dateOfBirth) {
//        super(username, firstname, lastname, gender);
//        this.dateOfBirth = dateOfBirth;
//        this.previousAppointments = new TreeSet<>();
//        this.upcomingAppointments = new TreeSet<>();
//        this.seenDoctors = new HashSet<Doctor>();
//        this.observers = new ArrayList<Observer>();
//    }

    Patient(String username, String firstname, String lastname) { // TEMPORARY
        super(username, firstname, lastname);
    }

    public void addSeenDoctor(Doctor doctor) {
        seenDoctors.add(doctor);
    }

    public void bookAppointment(Appointment appointment) {
        attach(appointment.doctor);
        upcomingAppointments.add(appointment);
        notifyBooking(appointment);
    }

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void dettach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyBooking(Appointment appointment) {
        for (Observer o : observers) {
            o.updateBooking(appointment);
        }
    }
}
