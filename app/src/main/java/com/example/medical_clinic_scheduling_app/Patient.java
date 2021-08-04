package com.example.medical_clinic_scheduling_app;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Patient extends Person {
    private Date dateOfBirth;
    private List<String> prevAppointmentIDs, upcomingAppointmentIDs, seenDoctorIDs;
    private List<String> observers;

    Patient(String username, String firstname, String lastname, String gender, Date dateOfBirth, String uid) {
        super(username, firstname, lastname, gender, Constants.PERSON_TYPE_PATIENT, uid);
        this.dateOfBirth = dateOfBirth;

        this.prevAppointmentIDs = new ArrayList<String>();
        this.upcomingAppointmentIDs = new ArrayList<String>();
        this.seenDoctorIDs = new ArrayList<String>();
        this.observers = new ArrayList<String>();
    }

    // Getters/setters:
    // dateOfBirth
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    // prevAppointmentIDs
    public List<String> getPrevAppointmentIDs() {
        return this.prevAppointmentIDs;
    }
    public void addPrevAppointment(Appointment prevAppt) {
        this.prevAppointmentIDs.add(prevAppt.getAppointmentID());
    }
    // upcomingAppointmentIDs
    public List<String> getUpcomingAppointmentIDs() {
        return this.upcomingAppointmentIDs;
    }
    public void addUpcomingAppointment(Appointment upcomingAppt) {
        addUpcomingAppointment(upcomingAppt.getAppointmentID());
    }
    public void addUpcomingAppointment(String upcomingApptID) {
        this.upcomingAppointmentIDs.add(upcomingApptID);
    }
    public void removeUpcomingAppointment(Appointment upcomingAppt) {
        this.upcomingAppointmentIDs.remove(upcomingAppt.getAppointmentID());
    }
    // seenDoctorIDs
    public List<String> getSeenDoctorIDs() {
        return seenDoctorIDs;
    }
    public void addSeenDoctor(Doctor doctor) {
        this.seenDoctorIDs.add(doctor.getID());
    }
    public void addSeenDoctor(String doctorID) {
        this.seenDoctorIDs.add(doctorID);
    }


    public void bookAppointment(Appointment appt) {
        attach(appt.getDoctorID());
//        this.upcomingAppointmentIDs.add(appt.hashCode());
        addUpcomingAppointment(appt);
        notifyBooking(appt);
    }

    // Observers
    public void attach(String observerID) {
        this.observers.add(observerID);
    }

    public void detach(String observerID) {
        this.observers.remove(observerID);
    }

    public void notifyBooking(Appointment appt) {
        for (String observerID : this.observers) {
            // Access Firebase to get user w/ observerID
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS);

            ref.child(observerID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Doctor user = snapshot.getValue(Doctor.class);

                    if (user != null) {
                        user.updateBooking(appt);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // ERROR: Doctor w/ observerID does not exist
                }
            });
        }
    }
}
