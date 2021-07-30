package com.example.medical_clinic_scheduling_app;

public interface Subject {
    void attach(Observer o);

    void dettach(Observer o);

    void notifyBooking(Appointment appointment);
}
