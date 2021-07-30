package com.example.medical_clinic_scheduling_app;

import java.util.Date;

public class Person {
    private String username;
//    private String password;
    private String firstname;
    private String lastname;
//    private String gender;
//    private Date dateOfBirth;

    Person(String username, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }
//    Person(String username, String password, String firstname, String lastname, String gender, Date dateOfBirth) {
//        this.username = username;
//        this.password = password;
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.gender = gender;
//        this.dateOfBirth = dateOfBirth;
//    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
