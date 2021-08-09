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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Appointment implements Comparable<Appointment> {
    private Date date;
    private String doctorID, patientID, appointmentID;
    private static int DAY_IN_MILLISECONDS = 86400000;

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

    public static void generateAvailableAppointmentsForOneDoctor(Doctor doctor, Date generateApptDate){
        // Generates one day of appointments for all doctors (9am, 11am, 1pm, 3pm)
        int day = DateUtility.getDay(generateApptDate);
        int month = DateUtility.getMonth(generateApptDate);
        int year = DateUtility.getYear(generateApptDate);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        for (int j = 0; j < 5; j++) { //Generating the day's appts 9,11am,1,3,5pm
            Appointment.generateAvailableAppointment(c.getTime(), doctor);
            c.add(Calendar.HOUR_OF_DAY, 2);
        }
    }

    public static void updateAvailableAppointmentsForAllDoctors() {
        //Checks to see if the last updated (availability) of doctor date and current date
        //to see if there is still 7 days worth of appointments!
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                        for (DataSnapshot userChild : usersSnapshot.getChildren()) {
                            String type = userChild.child(Constants.FIREBASE_PATH_USERS_TYPE).getValue(String.class);
                            Date lastUpdated = userChild.child(Constants.FIREBASE_PATH_USERS_LAST_AUTO_GENERATED_APPT_DATE).getValue(Date.class);
                            Calendar sevenDaysAhead = Calendar.getInstance();//Since we automatically generated 7 weeks of appts when doctor register
                            sevenDaysAhead.add(Calendar.DAY_OF_MONTH, 7);

                            //If the user is a doctor, fetch then call generateAvailableAppointment for available time slots
                            if (type.equals(Constants.PERSON_TYPE_DOCTOR)
                            && Math.abs(lastUpdated.getTime() - sevenDaysAhead.getTimeInMillis()) >= DAY_IN_MILLISECONDS ){
                                Doctor doctor = userChild.getValue(Doctor.class);
                                generateAvailableAppointmentsForOneDoctor(doctor, sevenDaysAhead.getTime());
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
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot apptSnapshot) {
                for (DataSnapshot apptChild : apptSnapshot.getChildren()) { // Loop through all appointments
                    Appointment appt = apptChild.getValue(Appointment.class);

                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS) // Prevent expiring appointment that is already expired
                            .child(appt.getAppointmentID())
                            .child(Constants.FIREBASE_PATH_APPOINTMENTS_PASSED)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot passedSnapshot) {
                                    if (!passedSnapshot.getValue(boolean.class)) { // Appointment is not expired yet (ie. not updated in Firebase)
                                        if (appt != null && appt.date != null && appt.isPassed()) { // Appointment has passed
                                            if (appt.isBooked()) { // Appointment is passed & booked
                                                String patientID = appt.getPatientID();
                                                String doctorID = appt.getDoctorID();
                                                String apptID = appt.getAppointmentID();

                                                // Update appointment (ie. update isPassed())
                                                FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_APPOINTMENTS)
                                                        .child(apptID)
                                                        .setValue(appt);

                                                // Patient:
                                                // Remove appointment from upcomingAppointmentIDs
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
                                                        upcomingApptIDs.remove(apptID);
                                                        patientUpcomingApptIDsRef.setValue(upcomingApptIDs);
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                                // Add appointment to prevAppointmentIDs
                                                DatabaseReference patientPrevApptIDsRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                                        .child(patientID)
                                                        .child(Constants.FIREBASE_PATH_USERS_APPTS_PREV);
                                                patientPrevApptIDsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot patientPrevApptIDsSnapshot) {
                                                        List<String> prevApptIDs = new ArrayList<>();
                                                        for (DataSnapshot patientPrevApptIDChild : patientPrevApptIDsSnapshot.getChildren()) {
                                                            prevApptIDs.add(patientPrevApptIDChild.getValue(String.class));
                                                        }
                                                        prevApptIDs.add(apptID);
                                                        patientPrevApptIDsRef.setValue(prevApptIDs);
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                                // Add doctorID to seenDoctorIDs
                                                DatabaseReference patientSeenDoctorIDsRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                                        .child(patientID)
                                                        .child(Constants.FIREBASE_PATH_USERS_SEEN_DOCTORS);
                                                patientSeenDoctorIDsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot patientSeenDoctorIDsSnapshot) {
                                                        List<String> seenDoctorIDs = new ArrayList<>();
                                                        for (DataSnapshot patientSeenDoctorIDChild : patientSeenDoctorIDsSnapshot.getChildren()) {
                                                            seenDoctorIDs.add(patientSeenDoctorIDChild.getValue(String.class));
                                                        }
                                                        seenDoctorIDs.add(doctorID);
                                                        patientSeenDoctorIDsRef.setValue(seenDoctorIDs);
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                                // Doctor:
                                                // Remove appointment from upcomingAppointmentIDs
                                                DatabaseReference doctorUpcomingApptIDsRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                                        .child(doctorID)
                                                        .child(Constants.FIREBASE_PATH_USERS_APPTS_UPCOMING);
                                                doctorUpcomingApptIDsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot doctorUpcomingApptIDsSnapshot) {
                                                        List<String> upcomingApptIDs = new ArrayList<>();
                                                        for (DataSnapshot doctorUpcomingApptIDChild : doctorUpcomingApptIDsSnapshot.getChildren()) {
                                                            upcomingApptIDs.add(doctorUpcomingApptIDChild.getValue(String.class));
                                                        }
                                                        upcomingApptIDs.remove(apptID);
                                                        doctorUpcomingApptIDsRef.setValue(upcomingApptIDs);
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                                // Add appointment to prevAppointmentIDs
                                                DatabaseReference doctorPrevApptIDsRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                                        .child(doctorID)
                                                        .child(Constants.FIREBASE_PATH_USERS_APPTS_PREV);
                                                doctorPrevApptIDsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot doctorPrevApptIDsSnapshot) {
                                                        List<String> prevApptIDs = new ArrayList<>();
                                                        for (DataSnapshot doctorPrevApptIDChild : doctorPrevApptIDsSnapshot.getChildren()) {
                                                            prevApptIDs.add(doctorPrevApptIDChild.getValue(String.class));
                                                        }
                                                        prevApptIDs.add(apptID);
                                                        doctorPrevApptIDsRef.setValue(prevApptIDs);
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                                // Add patientID to seenPatientIDs
                                                DatabaseReference doctorSeenDoctorIDsRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS)
                                                        .child(doctorID)
                                                        .child(Constants.FIREBASE_PATH_USERS_SEEN_PATIENTS);
                                                doctorSeenDoctorIDsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot doctorSeenPatientIDsSnapshot) {
                                                        List<String> seenPatientIDs = new ArrayList<>();
                                                        for (DataSnapshot doctorSeenPatientIDChild : doctorSeenPatientIDsSnapshot.getChildren()) {
                                                            seenPatientIDs.add(doctorSeenPatientIDChild.getValue(String.class));
                                                        }
                                                        seenPatientIDs.add(patientID);
                                                        doctorSeenDoctorIDsRef.setValue(seenPatientIDs);
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                            } else { // Appointment has passed but not been booked
                                                DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_PATH_USERS).child(appt.getDoctorID());
                                                doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot doctorSnapshot) {
                                                        Doctor doctor = doctorSnapshot.getValue(Doctor.class);
                                                        if (doctor != null) {
                                                            doctor.removeAvailableAppointment(appt); // Remove passed&unbooked appt from doctor.availableAppointmentIDs
                                                            doctorRef.setValue(doctor); // Send updated doctor back to Firebase
                                                        }
                                                        apptChild.getRef().removeValue(); // Remove passed&unbooked appt from Firebase
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error2) {
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