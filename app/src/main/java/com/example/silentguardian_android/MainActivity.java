package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

import com.example.silentguardian_android.Bluetooth.BluetoothMainActivity;
import com.example.silentguardian_android.fragments.InsertPasswordCheckFragment;

public class MainActivity extends AppCompatActivity {

    protected Button profileButton;
    protected Button thresholdButton;



    protected Button allclearButton;

    protected SharePreferenceHelper sharePreferenceHelper;

    protected Button sendMessageButton;

    protected TextView longitudeTextView;
    protected TextView latitudeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        thresholdButton = findViewById(R.id.thresholdButton);

        allclearButton = findViewById(R.id.allClearButton);



        sharePreferenceHelper = new SharePreferenceHelper(this);
        sendMessageButton = findViewById(R.id.sendMessageButton);


        longitudeTextView = findViewById(R.id.longtextView);
        latitudeTextView = findViewById(R.id.latTextView);




        thresholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checking if password matches from user to sharedpreferences

                Intent intent = new Intent(MainActivity.this, ThresholdSettingActivity.class);
                intent.putExtra("THRESHOLDVAL", 1);

                startActivity(intent);


            }
        });

        //permission check
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.VIBRATE
        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);



        //if the user doesn't have an account existing, this if statement takes them to profile activity to create first profile
        if (sharePreferenceHelper.userNameReturn() == null) {

            Intent intent = new Intent(MainActivity.this, profileActivity.class);
            startActivity(intent);
        } else {
            InsertPasswordCheckFragment dialog = new InsertPasswordCheckFragment();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "InsertPasswordCheck");


        }


        allclearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, allClearActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        final messageGPSHelper gpsHelper;
        gpsHelper = new messageGPSHelper(this);
        longitudeTextView.setText("Longitude: " + gpsHelper.getLong());
        latitudeTextView.setText("Latitude: " + gpsHelper.getLat());

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.SEND_SMS}, 3);

                //really dumb way to write to this function neeed to refactor
                gpsHelper.sendMessage("7786898291", "Test");
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.bluetoothSettingsdropdown:
                 Intent intent = new Intent(MainActivity.this, BluetoothMainActivity.class);
                startActivity(intent);
                return true;
            case R.id.profileSettingdropdown:
                Intent intent1 = new Intent(MainActivity.this, profileActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}








