package com.example.silentguardian_android.Tutorial;

public class MyImage {

    private String title, description;
    private int myPicture;

    public MyImage(String title, String description, int myPicture) {
        this.title = title;
        this.description = description;
        this.myPicture = myPicture;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getMyPicture() {
        return myPicture;
    }
}
