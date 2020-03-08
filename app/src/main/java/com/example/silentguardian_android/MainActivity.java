package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

import com.example.silentguardian_android.Bluetooth.BluetoothMainActivity;
import com.example.silentguardian_android.fragments.InsertPasswordCheckFragment;

public class MainActivity extends AppCompatActivity {

    protected Button profileButton;
    protected Button thresholdButton;
    protected Button bubbleButton;

    protected Button mBluetoothButton;

    protected SharePreferenceHelper sharePreferenceHelper;

    protected Button sendMessageButton;

    protected TextView longitudeTextView;
    protected TextView latitudeTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileButton = findViewById(R.id.profileButton);
        thresholdButton = findViewById(R.id.thresholdButton);
        bubbleButton = findViewById(R.id.bubbleButton);
        mBluetoothButton = findViewById(R.id.BLEbutton);
        mBluetoothButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BluetoothMainActivity.class);
                startActivity(intent);
            }
        });

        sharePreferenceHelper = new SharePreferenceHelper(this);
        sendMessageButton = findViewById(R.id.sendMessageButton);


        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        longitudeTextView = findViewById(R.id.longtextView);
        latitudeTextView = findViewById(R.id.latTextView);




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

               //checking if password matches from user to sharedpreferences

               InsertPasswordCheckFragment dialog = new InsertPasswordCheckFragment();

               dialog.show(getSupportFragmentManager(), "InsertPasswordCheck");


               // if (InsertPasswordCheckFragment.)

              // Intent intent = new Intent(MainActivity.this , ThresholdActivity.class);
               //startActivity(intent);
           }
       });

       bubbleButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this , contactActivity.class);
               startActivity(intent);
           }
       });


       //if the user doesn't have an account existing, this if statement takes them to profile activity to create first profile
        if(sharePreferenceHelper.userNameReturn() == null){
            Intent intent = new Intent(MainActivity.this , profileActivity.class);
            startActivity(intent);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


        // Upon resuming the mainActivity, if the user has a name saved into sharedpreferences, then replace the text on the profile button to their name.
        if(sharePreferenceHelper.userNameReturn()!= null) {
            profileButton.setText(sharePreferenceHelper.userNameReturn() + "'s Profile Page ");
        }

        final messageGPSHelper gpsHelper;
        gpsHelper = new messageGPSHelper(this);
        longitudeTextView.setText("Longitude: " + gpsHelper.getLong());
        latitudeTextView.setText("Latitude: " +gpsHelper.getLat());

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions((Activity)v.getContext(), new String[]{Manifest.permission.SEND_SMS}, 3);

                //really dumb way to write to this function neeed to refactor
                gpsHelper.sendMessage("7786898291","Test" );
            }
        });

    }



       
    }
