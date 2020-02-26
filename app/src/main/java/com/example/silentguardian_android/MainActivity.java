package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected Button profileButton;
    protected Button thresholdButton;
    protected Button bubbleButton;


    protected TextView longitudeTextView;
    protected TextView latitudeTextView;

    messageGPSHelper gpsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileButton = findViewById(R.id.profileButton);
        thresholdButton = findViewById(R.id.thresholdButton);
        bubbleButton = findViewById(R.id.bubbleButton);

        gpsHelper = new messageGPSHelper(this);

        longitudeTextView = findViewById(R.id.longtextView);
        latitudeTextView = findViewById(R.id.latTextView);

        longitudeTextView.setText("Longitude: " + gpsHelper.getLong());
        latitudeTextView.setText("Latitude: " +gpsHelper.getLat());


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , profileActivity.class);
                startActivity(intent);
            }
        });


       thresholdButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this , ThresholdActivity.class);
               startActivity(intent);
           }
       });

       bubbleButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this , BubbleActivity.class);
               startActivity(intent);
           }
       });

    }


}
