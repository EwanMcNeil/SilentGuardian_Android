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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

import com.example.silentguardian_android.Bluetooth.BluetoothMainActivity;
import com.example.silentguardian_android.fragments.InsertPasswordCheckFragment;

public class MainActivity extends AppCompatActivity {





    protected ImageButton thresholdimageButton;
    protected ImageButton allclearImageButton;
    protected SharePreferenceHelper sharePreferenceHelper;
    protected TextView iAmSafeText;
    protected TextView setUpText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        thresholdimageButton = findViewById(R.id.thresholdimageButton);
        allclearImageButton = findViewById(R.id.safeimageButton);
        iAmSafeText = findViewById(R.id.iAmSafeTextView);
        setUpText = findViewById(R.id.setUpTextView);

        sharePreferenceHelper = new SharePreferenceHelper(this);


        thresholdimageButton.setOnClickListener(new View.OnClickListener() {

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
        }
        else if(!sharePreferenceHelper.hasLoggedIn()){

                InsertPasswordCheckFragment dialog = new InsertPasswordCheckFragment();
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "InsertPasswordCheck");

        }




        allclearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, allClearActivity.class);
                startActivity(intent);

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharePreferenceHelper.logOut();//trying to find a way to reset the log in this way
    }

    ///code for the menu
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
            case R.id.sendTestMessageDropDown:
                final messageGPSHelper gpsHelper;
                gpsHelper = new messageGPSHelper(this);
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 3);
                        gpsHelper.sendMessage("7786898291", "Test");
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}








