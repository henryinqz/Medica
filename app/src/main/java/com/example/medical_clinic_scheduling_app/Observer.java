package com.example.medical_clinic_scheduling_app;

public interface Observer {
    void updateBooking(Appointment appointment);

    void updatePassing(Appointment appointment);
}
