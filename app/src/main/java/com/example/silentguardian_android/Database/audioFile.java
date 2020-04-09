package com.example.silentguardian_android.Database;

public class audioFile {

    int ID;
    String date;
    String file;

    public audioFile(int ID, String date, String file) {
        this.ID = ID;
        this.date = date;
        this.file = file;
    }

    public audioFile(String date, String file) {
        this.date = date;
        this.file = file;
    }

    public int getID() {
        return ID;
    }

    public String getDate() {
        return date;
    }

    public String getFile() {
        return file;
    }
}
