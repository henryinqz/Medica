package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Patient extends Person implements Subject {
    private Date dateOfBirth;
    private List<Integer> previousAppointments;
    private List<Integer> upcomingAppointments;
    private List<Integer> seenDoctors;
    private List<Observer> observers;

    Patient(String username, String firstname, String lastname, String gender, Date dateOfBirth) {
        super(username, firstname, lastname, gender, Constants.PERSON_TYPE_PATIENT);
        this.dateOfBirth = dateOfBirth;

        this.previousAppointments = new ArrayList<Integer>();
        this.upcomingAppointments = new ArrayList<Integer>();
        this.seenDoctors = new ArrayList<Integer>();
        this.observers = new ArrayList<Observer>();
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void addSeenDoctor(Doctor doctor) {
        seenDoctors.add(doctor.hashCode());
    }

    public void bookAppointment(Appointment appointment) {
        attach(appointment.doctor);
        upcomingAppointments.add(appointment.hashCode());
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
