package com.example.silentguardian_android.Database;

public class Person {


    private int ID;
    private String Name;
    private String PhoneNumber;
    private int threshold;
    private String password;

    public Person(int ID, String name, String phoneNumber, int threshold) {
        this.ID = ID;
        Name = name;
        PhoneNumber = phoneNumber;
    }

    public Person(String name, String phoneNumber, int threshold) {
        Name = name;
        PhoneNumber = phoneNumber;
    }



    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
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
