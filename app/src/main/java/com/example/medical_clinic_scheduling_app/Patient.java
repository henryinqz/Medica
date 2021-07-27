package com.example.medical_clinic_scheduling_app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Patient extends Person {
    private List<Appointment> previousAppointments;
    private List<Appointment> upcomingAppointments;
    private HashSet<Doctor> seenDoctors;

    Patient(String name, String gender, Date dateOfBirth) {
        super(name, gender, dateOfBirth);
        previousAppointments = new ArrayList<Appointment>();
        upcomingAppointments = new ArrayList<Appointment>();
        seenDoctors = new HashSet<Doctor>();
    }
}
