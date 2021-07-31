package com.example.medical_clinic_scheduling_app;

public abstract class Person {
    private String username, firstName, lastName, gender;

    Person(String username, String firstname, String lastname, String gender) {
        this.username = username;
        this.firstName = firstname;
        this.lastName = lastname;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
