package com.example.silentguardian_android.Tutorial;

public class MyImage {

    private String title, description;
    private int id;

    public MyImage(String title, String description, int id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
