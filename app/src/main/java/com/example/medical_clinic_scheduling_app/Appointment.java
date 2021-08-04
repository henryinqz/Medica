package com.example.medical_clinic_scheduling_app;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

public class Appointment implements Comparable<Appointment> {
    private Date date;
    private String doctorID, patientID, appointmentID;

    private Appointment() {

    }
    Appointment(Date date, Doctor doctor, Patient patient) {
        this.date = date;
        this.doctorID = doctor.getID();

        if (patient == null) { // i.e. appointment is available
            this.setPatientID("");
        } else {
            this.setPatientID(patient.getID());
        }

        this.appointmentID = hashCode() + ""; // TODO: Make a better ID?
    }

    // Getters/setters:
    // Date
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    // doctorID
    public String getDoctorID() {
        return this.doctorID;
    }
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    // patientID
    public String getPatientID() {
        return this.patientID;
    }
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    // appointmentID
    public String getAppointmentID() {
        return this.appointmentID;
    }
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public boolean isBooked() {
        return !this.patientID.isEmpty();
    }

    // Static methods:
    public static Appointment generateAvailableAppointment(Date date, Doctor doctor) {
        if (date != null && doctor != null) {
            Appointment availableAppt = new Appointment(date, doctor, null);

            // Add to Firebase for appointments
            FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS)
                    .child(availableAppt.getAppointmentID())
                    .setValue(availableAppt)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) { // Added appointment
                                doctor.addAvailableAppointment(availableAppt); // TODO: For some reason, this crashes the program :/

                                // Update doctor Firebase
                                FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                        .child(doctor.getID())
                                        .setValue(doctor);
                            } else { // Failed to add appointment
                                Log.i("appt_error", "Failed to add new available appointment to Firebase");
                            }
                        }
                    });

            return availableAppt;
        } else {
            return null;
        }

    }

    public static void bookAppointment(String appointmentID, String patientID) {
        // Get appt
        DatabaseReference apptRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS).child(appointmentID);
        apptRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Appointment appt = snapshot.getValue(Appointment.class);
                if (appt != null) {
                    appt.setPatientID(patientID); // Update patientID in appointment
                    apptRef.setValue(appt); // Send back to Firebase

                    // Get patient
                    DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS).child(patientID);
                    patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Patient patient = snapshot.getValue(Patient.class);
                            if (patient != null) {
                                patient.addUpcomingAppointment(appointmentID); // Add appointment to patient upcoming
                                patientRef.setValue(patient); // Send back to Firebase
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { // Error: patient doesn't exist
                        }
                    });

                    // Get doctor
                    DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS).child(appt.getDoctorID());
                    doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Doctor doctor = snapshot.getValue(Doctor.class);
                            if (doctor != null) {
                                doctor.addUpcomingAppointment(appointmentID); // Add appointment to doctor upcoming
                                doctor.removeAvailableAppointment(appointmentID); // Remove appointment from doctor available
                                doctorRef.setValue(doctor); // Send back to Firebase
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { // Error: doctor doesn't exist
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Error: patient doesn't exist
            }
        });
    }
    public static void bookAppointment(String appointmentID, Patient patient) {
        bookAppointment(appointmentID, patient.getID());
    }
    public static void bookAppointment(Appointment appt, Patient patient) {
        bookAppointment(appt.getAppointmentID(), patient.getID());
    }
    public static void bookAppointment(Appointment appt, String patientID) {
        bookAppointment(appt.getAppointmentID(), patientID);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return getDate().equals(that.getDate()) && getDoctorID().equals(that.getDoctorID()) && getPatientID().equals(that.getPatientID()) && getAppointmentID().equals(that.getAppointmentID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getDoctorID(), getPatientID());
    }

    @Override
    public int compareTo(Appointment o) {
        return this.date.compareTo(o.date);
    }
}