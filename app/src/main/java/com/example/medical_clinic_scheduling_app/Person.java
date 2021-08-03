package com.example.medical_clinic_scheduling_app;

public class Person {
    private String username, firstName, lastName, gender, type, id;

    Person() {
    }
    Person(String username, String firstname, String lastname, String gender, String type, String id) {
        this.username = username;
        this.firstName = firstname;
        this.lastName = lastname;
        this.gender = gender;

        this.type = type;
        this.id = id;
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

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getID() {
        return this.id;
    }
    public void setID(String id) {
        this.id = id;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Person)) return false;
//        Person person = (Person) o;
//        return getUsername().equals(person.getUsername()) && getType().equals(person.getType());
//    }
//    @Override
//    public int hashCode() {
//        return Objects.hash(getUsername(), getType());
//    }

    @Override
    public String toString(){
        return this.firstName + " " + this.lastName;
    }
}
