package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Patient extends Person implements Subject {
    private Date dateOfBirth;
    private List<Integer> prevAppointmentIDs;
    private List<Integer> upcomingAppointmentIDs;
    private List<Integer> seenDoctorIDs;
    private List<Observer> observers;

    Patient(String username, String firstname, String lastname, String gender, Date dateOfBirth) {
        super(username, firstname, lastname, gender, Constants.PERSON_TYPE_PATIENT);
        this.dateOfBirth = dateOfBirth;

        this.prevAppointmentIDs = new ArrayList<Integer>();
        this.upcomingAppointmentIDs = new ArrayList<Integer>();
        this.seenDoctorIDs = new ArrayList<Integer>();
        this.observers = new ArrayList<Observer>();
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void addSeenDoctor(Doctor doctor) {
        seenDoctorIDs.add(doctor.hashCode());
    }

    public void bookAppointment(Appointment appointment) {
        attach(appointment.doctor);
        upcomingAppointmentIDs.add(appointment.hashCode());
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
