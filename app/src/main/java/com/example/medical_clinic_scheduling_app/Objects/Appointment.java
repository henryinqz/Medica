package com.example.medical_clinic_scheduling_app.Objects;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.medical_clinic_scheduling_app.Constants;
import com.example.medical_clinic_scheduling_app.DateUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

        this.appointmentID = hashCode() + "";
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

    public boolean isPassed() {
        return this.date.compareTo(new Date(System.currentTimeMillis())) < 0;
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
                                doctor.addAvailableAppointment(availableAppt);

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

    public static void generateAvailableAppointmentsAllDoctors(Date generateApptsDate) { // Generates one day of appointments for all doctors (9am, 11am, 1pm, 3pm)
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                        for (DataSnapshot userChild : usersSnapshot.getChildren()) {
                            String type = userChild.child(Constants.FIREBASE_PATH_USERS_TYPE).getValue(String.class);

                            //If the user is a doctor, fetch then call generateAvailableAppointment for available time slots
                            if (type.equals(Constants.PERSON_TYPE_DOCTOR)){
                                Doctor doctor = userChild.getValue(Doctor.class);

                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                int day = DateUtility.getDay(generateApptsDate);
                                int month = DateUtility.getMonth(generateApptsDate);
                                int year = DateUtility.getYear(generateApptsDate);

                                // TODO: Check if appointment being generated will be a duplicate?
                                String datestring = DateUtility.simpleDateFormater(day, month, year, 9, 0, 0);
                                try { // New available appointment: 9am
                                    Date shift1 = format.parse(datestring);
                                    generateAvailableAppointment(shift1, doctor);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                datestring = DateUtility.simpleDateFormater(day, month, year, 11, 0, 0);
                                try { // New available appointment: 11am
                                    Date shift2 = format.parse(datestring);
                                    generateAvailableAppointment(shift2, doctor);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                datestring = DateUtility.simpleDateFormater(day, month, year, 13, 0, 0);
                                try { // New available appointment: 1pm
                                    Date shift3 = format.parse(datestring);
                                    generateAvailableAppointment(shift3, doctor);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                datestring = DateUtility.simpleDateFormater(day, month, year, 15, 0, 0);
                                try { // New available appointment: 3pm
                                    Date shift4 = format.parse(datestring);
                                    generateAvailableAppointment(shift4, doctor);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("appt_error", "Failed to get Doctor info from Firebase");
                    }
                });
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

                    // Patient:
                    // Add appointment to upcomingAppointmentIDs
                    DatabaseReference patientUpcomingApptIDsRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(patientID)
                            .child(Constants.FIREBASE_PATH_USERS_APPTS_UPCOMING);
                    patientUpcomingApptIDsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot patientUpcomingApptIDsSnapshot) {
                            List<String> upcomingApptIDs = new ArrayList<>();
                            for (DataSnapshot patientUpcomingApptIDChild : patientUpcomingApptIDsSnapshot.getChildren()) {
                                upcomingApptIDs.add(patientUpcomingApptIDChild.getValue(String.class));
                            }
                            upcomingApptIDs.add(appointmentID);
                            patientUpcomingApptIDsRef.setValue(upcomingApptIDs);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
//                    DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS).child(patientID);
//                    patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Log.i("crash_patient", "HI");
//                            Patient patient = snapshot.getValue(Patient.class);
//                            if (patient != null) {
//                                patient.addUpcomingAppointment(appointmentID); // Add appointment to patient upcoming
//                                patientRef.setValue(patient); // Send back to Firebase
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) { // Error: patient doesn't exist
//                            Log.i("appt_error", "Failed to get patient from Firebase");
//                        }
//                    });

                    // Doctor:
                    // Add appointment to upcomingAppointmentIDs
                    DatabaseReference doctorUpcomingApptIDsRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(appt.getDoctorID())
                            .child(Constants.FIREBASE_PATH_USERS_APPTS_UPCOMING);
                    doctorUpcomingApptIDsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot doctorUpcomingApptIDsSnapshot) {
                            List<String> upcomingApptIDs = new ArrayList<>();
                            for (DataSnapshot doctorUpcomingApptIDChild : doctorUpcomingApptIDsSnapshot.getChildren()) {
                                upcomingApptIDs.add(doctorUpcomingApptIDChild.getValue(String.class));
                            }
                            upcomingApptIDs.add(appointmentID);
                            doctorUpcomingApptIDsRef.setValue(upcomingApptIDs);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    // Remove appointment from availableAppointmentIDs
                    DatabaseReference doctorAvailableApptIDsRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                            .child(appt.getDoctorID())
                            .child(Constants.FIREBASE_PATH_USERS_APPTS_AVAILABLE);
                    doctorAvailableApptIDsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot doctorAvailableApptIDsSnapshot) {
                            List<String> availableApptIDs = new ArrayList<>();
                            for (DataSnapshot doctorAvailableApptIDChild : doctorAvailableApptIDsSnapshot.getChildren()) {
                                availableApptIDs.add(doctorAvailableApptIDChild.getValue(String.class));
                            }
                            availableApptIDs.remove(appointmentID);
                            doctorAvailableApptIDsRef.setValue(availableApptIDs);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

//                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
//                            .child(appt.getDoctorID())
//                            .child(Constants.FIREBASE_PATH_USERS_APPTS_UPCOMING)
//                            .child(appointmentID).push();
//                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
//                            .child(appt.getDoctorID())
//                            .child(Constants.FIREBASE_PATH_USERS_APPTS_AVAILABLE)
//                            .child(appointmentID).removeValue();

//                    DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS).child(appt.getDoctorID());
//                    doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Log.i("crash_doctor", "HI");
//                            Doctor doctor = snapshot.getValue(Doctor.class);
//                            if (doctor != null) {
//                                doctor.addUpcomingAppointment(appointmentID); // Add appointment to doctor upcoming
//                                doctor.removeAvailableAppointment(appointmentID); // Remove appointment from doctor available
//                                doctorRef.setValue(doctor); // Send back to Firebase
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) { // Error: doctor doesn't exist
//                            Log.i("appt_error", "Failed to get doctor from Firebase");
//                        }
//                    });
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
    public static void expireAppointments(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for (DataSnapshot child : snapshot.getChildren()){
                    Appointment appt = child.getValue(Appointment.class);

                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS)
                            .child(appt.getAppointmentID())
                            .child(Constants.FIREBASE_PATH_APPOINTMENTS_PASSED)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot passedSnapshot) {
                                    if (!passedSnapshot.getValue(boolean.class)) { // Not expired already
                                        if (appt != null && appt.date != null && appt.isPassed()){
                                            if (!appt.isBooked()) { // Appointment has passed but not been booked
                                                child.getRef().removeValue(); // Remove appt from Firebase

                                                FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                                        .child(appt.getDoctorID())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                                Doctor doctor = snapshot2.getValue(Doctor.class);
                                                                if (doctor != null) {
                                                                    doctor.removeAvailableAppointment(appt); // Remove appt from doctor.availableAppointmentIDs
                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error2) {
                                                            }
                                                        });
                                            } else {
                                                String patientID = appt.getPatientID();
                                                String doctorID = appt.getDoctorID();
                                                String apptID = appt.getAppointmentID();

                                                // Update appointment (ie. update isPassed())
                                                FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS)
                                                        .child(apptID)
                                                        .setValue(appt);

                                                // Get patient
                                                DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS).child(patientID);
                                                patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        Patient patient = snapshot.getValue(Patient.class);
                                                        if (patient != null) {
                                                            patient.removeUpcomingAppointment(appt); // Remove appointment from patient upcoming
                                                            patient.addPrevAppointment(appt); // Add appointment to patient prev
                                                            patient.addSeenDoctor(doctorID); // Add seenDoctor to patient
                                                            patientRef.setValue(patient); // Send back to Firebase
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) { // Error: patient doesn't exist
                                                        Log.i("appt_error", "Failed to get patient from Firebase");
                                                    }
                                                });

                                                // Get doctor
                                                DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS).child(appt.getDoctorID());
                                                doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        Doctor doctor = snapshot.getValue(Doctor.class);
                                                        if (doctor != null) {
                                                            doctor.removeUpcomingAppointment(appt); // Remove appointment from doctor upcoming
                                                            doctor.addPrevAppointment(appt); // Add appointment to doctor prev
                                                            doctor.addSeenPatient(patientID); // Add seenPatient to doctor
                                                            doctorRef.setValue(doctor); // Send back to Firebase
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) { // Error: doctor doesn't exist
                                                        Log.i("appt_error", "Failed to get doctor from Firebase");
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return getDate().equals(that.getDate()) && getDoctorID().equals(that.getDoctorID()) && getPatientID().equals(that.getPatientID()) && getAppointmentID().equals(that.getAppointmentID());
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getDoctorID(), getPatientID());
    }

    @Override
    public int compareTo(Appointment o) {
        return this.date.compareTo(o.date);
    }
}