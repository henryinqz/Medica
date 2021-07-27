package com.example.medical_clinic_scheduling_app;

import java.util.HashSet;
import java.util.List;

public class Doctor extends Person {
    private List<String> specializations;
    private HashSet<Patient> seenPatients;

    Doctor(String name, String gender, Date dateOfBirth, List<String> specializations) {
        super(name, gender, dateOfBirth);
        this.specializations = specializations;
        seenPatients = new HashSet<Patient>();
    }
}
