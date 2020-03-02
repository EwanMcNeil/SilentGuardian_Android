package com.example.silentguardian_android.Database;

public class Person {


    private int ID;
    private String Name;
    private String PhoneNumber;
    private int thresholdOne;
    private int thresholdTwo;
    private String password;

    public Person(int ID, String name, String phoneNumber, int thresholdOne, int thresholdTwo) {
        this.ID = ID;
        Name = name;
        PhoneNumber = phoneNumber;
        thresholdOne = thresholdOne;
        thresholdTwo = thresholdTwo;
    }

    public Person(String name, String phoneNumber, int thresholdOne, int thresholdTwo) {
        Name = name;
        PhoneNumber = phoneNumber;
        thresholdOne = thresholdOne;
        thresholdTwo = thresholdTwo;
    }
    public Person(String name, String phoneNumber) {
        Name = name;
        PhoneNumber = phoneNumber;
    }


    public int getThresholdOne() {
        return thresholdOne;
    }

    public void setThresholdOne(int thresholdOne) {
        this.thresholdOne = thresholdOne;
    }

    public int getThresholdTwo() {
        return thresholdTwo;
    }

    public void setThresholdTwo(int thresholdTwo) {
        this.thresholdTwo = thresholdTwo;
    }

    

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


}
