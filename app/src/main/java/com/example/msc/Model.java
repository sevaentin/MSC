package com.example.msc;

public class Model {
    private String id;
    private String LastName;
    private String FirstName;
    private String Status;
    private String BirthDate;
    private String Finger;

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getFinger() {
        return Finger;
    }

    public void setFinger(String finger) {
        Finger = finger;
    }
}
