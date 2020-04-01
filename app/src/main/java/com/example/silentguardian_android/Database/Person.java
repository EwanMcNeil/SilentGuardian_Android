package com.example.silentguardian_android.Database;

import androidx.annotation.Nullable;

public class Person {


    private int ID;
    private String Name;
    private String PhoneNumber;

    private int thresholdOne;//who did this loooooool
    private int thresholdTwo;
    private String password;

    public Person(int ID, String name, String phoneNumber, int thresholdOne1, int thresholdTwo1) {
        this.ID = ID;
        Name = name;
        PhoneNumber = phoneNumber;
        thresholdOne = thresholdOne1;
        thresholdTwo = thresholdTwo1;
    }

    public Person(String name, String phoneNumber, int thresholdOne1, int thresholdTwo1) {
        Name = name;
        PhoneNumber = phoneNumber;
        thresholdOne = thresholdOne1;
        thresholdTwo = thresholdTwo1;

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


    public Person getPerson(){
        Person temp = new Person(ID, Name, PhoneNumber, thresholdOne,thresholdTwo);
        return temp;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if(this == null)
            return false;
        if(obj == null || (obj.getClass()!= this.getClass()))
            return false;

        Person temp = (Person) obj;
        return
                 Name.equals(temp.Name)
                & PhoneNumber.equals(temp.PhoneNumber);
    }
}
