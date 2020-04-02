package com.example.silentguardian_android.Tutorial;

public class MyImage {

    private String title, description;
    private int myPicture;

    private boolean isGif;

    public MyImage(String title, String description, int myPicture) {
        this.title = title;
        this.description = description;
        this.myPicture = myPicture;
        isGif = false;
    }
    public MyImage(String title, String description, int myPicture,boolean isGif) {
        this.title = title;
        this.description = description;
        this.myPicture = myPicture;
        isGif = true;
    }
    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
    }

//    ImageView imageView = (ImageView) findViewById(R.id.image);
//        Glide.with(this).load(R.drawable.loader).asGif().into(imageView);

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
