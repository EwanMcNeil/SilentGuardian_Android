package com.example.silentguardian_android.Database;

public class Person {


    private int ID;
    private String Name;
    private String PhoneNumber;

    public Person(int ID, String name, String phoneNumber) {
        this.ID = ID;
        Name = name;
        PhoneNumber = phoneNumber;
    }

    public Person(String name, String phoneNumber) {
        Name = name;
        PhoneNumber = phoneNumber;
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
