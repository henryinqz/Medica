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

    private Patient() {
    }

//    Patient(String username, String firstname, String lastname, String gender, Date dateOfBirth) {
    Patient(String username, String firstname, String lastname, String gender) {
        super(username, firstname, lastname, gender, Constants.PERSON_TYPE_PATIENT);
//        this.dateOfBirth = dateOfBirth;
        this.previousAppointments = new TreeSet<>();
        this.upcomingAppointments = new TreeSet<>();
        this.seenDoctors = new HashSet<Doctor>();
        this.observers = new ArrayList<Observer>();
    }

//    public Date getDateOfBirth() {
//        return dateOfBirth;
//    }
//    public void setDateOfBirth(Date dateOfBirth) {
//        this.dateOfBirth = dateOfBirth;
//    }

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
