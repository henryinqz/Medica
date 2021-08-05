package com.example.medical_clinic_scheduling_app;

public class Constants {
    public static final String PERSON_TYPE_DOCTOR = "DOCTOR";
    public static final String PERSON_TYPE_PATIENT = "PATIENT";

    public static final String FIREBASE_PATH_USERS = "Users";
    public static final String FIREBASE_PATH_USERS_ID = "id";
    public static final String FIREBASE_PATH_USERS_FIRST_NAME = "firstName";
    public static final String FIREBASE_PATH_USERS_LAST_NAME = "lastName";
    public static final String FIREBASE_PATH_USERS_TYPE = "type";
    public static final String FIREBASE_PATH_USERS_GENDER = "gender";
    public static final String FIREBASE_PATH_USERS_APPTS_AVAILABLE = "availableAppointmentIDs";

    public static final String FIREBASE_PATH_DOCTORS_SPECIALIZATIONS = "specializations";

    public static final String FIREBASE_PATH_APPOINTMENTS = "Appointments";
    public static final String FIREBASE_PATH_APPOINTMENTS_DATE = "date";
    public static final String FIREBASE_PATH_APPOINTMENTS_PATIENT_ID = "patientID";
    public static final String FIREBASE_PATH_APPOINTMENTS_DOCTOR_ID = "doctorID";
}
