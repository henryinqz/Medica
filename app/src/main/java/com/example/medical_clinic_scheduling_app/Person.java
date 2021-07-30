package com.example.medical_clinic_scheduling_app;

import java.util.Date;

public class Person {
    private String username;
    private String firstname;
    private String lastname;
    private String gender;

//    Person(String username, String firstname, String lastname, String gender) {
//        this.username = username;
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.gender = gender;
//    }
    Person(String username, String firstname, String lastname) { // TEMPORARY
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

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
