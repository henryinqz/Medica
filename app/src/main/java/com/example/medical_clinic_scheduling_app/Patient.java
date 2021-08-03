package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Patient extends Person implements Subject {
    private Date dateOfBirth;
    private List<Integer> prevAppointmentIDs, upcomingAppointmentIDs, seenDoctorIDs;
    private List<Observer> observers;

    Patient(String username, String firstname, String lastname, String gender, Date dateOfBirth) {
        super(username, firstname, lastname, gender, Constants.PERSON_TYPE_PATIENT);
        this.dateOfBirth = dateOfBirth;

        this.prevAppointmentIDs = new ArrayList<Integer>();
        this.upcomingAppointmentIDs = new ArrayList<Integer>();
        this.seenDoctorIDs = new ArrayList<Integer>();
        this.observers = new ArrayList<Observer>();
    }

    // Getters/setters:
    // dateOfBirth
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    // prevAppointmentIDs
    public List<Integer> getPrevAppointmentIDs() {
        return prevAppointmentIDs;
    }
    public void addPrevAppointment(Appointment prevAppt) {
        this.prevAppointmentIDs.add(prevAppt.hashCode());
    }
    // upcomingAppointmentIDs
    public List<Integer> getUpcomingAppointmentIDs() {
        return upcomingAppointmentIDs;
    }
    public void addUpcomingAppointment(Appointment upcomingAppt) {
        this.upcomingAppointmentIDs.add(upcomingAppt.hashCode());
    }
    // seenDoctorIDs
    public List<Integer> getSeenDoctorIDs() {
        return seenDoctorIDs;
    }
    public void addSeenDoctor(Doctor doctor) { // TODO: Maybe private & only accessed by observers after appointment passes?
        this.seenDoctorIDs.add(doctor.hashCode());
    }
    // TODO: observers (? Not sure if this should be sent to Firebase)



    public void bookAppointment(Appointment appointment) {
        attach(appointment.doctor);
        upcomingAppointmentIDs.add(appointment.hashCode());
        notifyBooking(appointment);
    }

    // Observers
    @Override
    public void attach(Observer o) {
        observers.add(o);
    }
    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }
    @Override
    public void notifyBooking(Appointment appointment) {
        for (Observer o : observers) {
            o.updateBooking(appointment);
        }
    }
}
