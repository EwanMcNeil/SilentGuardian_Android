package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

import com.example.silentguardian_android.Bluetooth.BluetoothMainActivity;
import com.example.silentguardian_android.fragments.InsertPasswordCheckFragment;
import com.example.silentguardian_android.fragments.sendMessageFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {






    protected ImageButton allclearImageButton;
    protected SharePreferenceHelper sharePreferenceHelper;
    protected TextView iAmSafeText;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharePreferenceHelper = new SharePreferenceHelper(this);
        //initialzing the sentmessage
        if(sharePreferenceHelper.getMessageSent() == 2){
            sharePreferenceHelper.setMessageSent(0);
        }



        allclearImageButton = findViewById(R.id.safeimageButton);

        iAmSafeText = findViewById(R.id.iAmSafeTextView);
        sharePreferenceHelper = new SharePreferenceHelper(this);


        //permission check
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.VIBRATE,
                android.Manifest.permission.FOREGROUND_SERVICE
        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);





        //if the user doesn't have an account existing, this if statement takes them to profile activity to create first profile
        if (sharePreferenceHelper.userNameReturn() == null) {
            Intent intent = new Intent(MainActivity.this, profileActivity.class);
            startActivity(intent);

        }





        allclearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int recent = sharePreferenceHelper.getMessageSent();
                if(recent == 1) {
                    Intent intent = new Intent(MainActivity.this, allClearActivity.class);
                    startActivity(intent);
                }else{
                    sendMessageFragment dialog = new sendMessageFragment();
                    dialog.show(getSupportFragmentManager(), "sendmessage_fragment");
                }
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
     updateAllClearButton();

    }

    public void updateAllClearButton(){
        int output = sharePreferenceHelper.getMessageSent();


        if(output == 0){
            iAmSafeText.setText("I am in danger");
            allclearImageButton.setBackgroundResource(R.drawable.indanger);

            //needs to be changed to somthing else
        }
        else{
            iAmSafeText.setText("I am safe");
            allclearImageButton.setBackgroundResource(R.drawable.i_am_safe_image);
        }

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
        InsertPasswordCheckFragment dialog = new InsertPasswordCheckFragment();
        Bundle args = new Bundle();
        Intent intent;

        switch (item.getItemId()) {
            case R.id.bluetoothSettingsdropdown:

                args.putString("intent","bluetooth");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "password");
                return true;
            case R.id.profileSettingdropdown:
                args.putString("intent","profile");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "password");
                return true;

            case R.id.sendTestMessageDropDown:
                final messageGPSHelper gpsHelper;
                gpsHelper = new messageGPSHelper(this);
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 3);
                        gpsHelper.sendMessage("7786898291", "Test");
                return true;
            case R.id.switchLanguage:
                switchLanguage();
                return true;
            case R.id.thresholdsettingdropdown:
                //checking if password matches from user to sharedpreferences
                args.putString("intent","threshold");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "password");
                return true;

            case R.id.checkIndropdown:
                intent = new Intent(MainActivity.this, checkInActivity.class);
                startActivity(intent);
                return true;

            case R.id.recordingsActivity:
                intent = new Intent(MainActivity.this, AudioRecordTest.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void switchLanguage(){
        String current = Locale.getDefault().getLanguage();
        if(current == "fr"){
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            sharePreferenceHelper.saveLanguage("en");
            recreate();
        }
        else if(current == "en"){
            Locale locale = new Locale("fr");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            sharePreferenceHelper.saveLanguage("fr");
            recreate();
        }
    }



}








