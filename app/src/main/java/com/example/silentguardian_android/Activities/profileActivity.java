package com.example.silentguardian_android.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.silentguardian_android.Helpers.DatabaseHelper;
import com.example.silentguardian_android.Helpers.Person;
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.R;

import java.util.Locale;

public class profileActivity extends AppCompatActivity {

    protected EditText userEditText;
    protected EditText passwordEditText;
    protected Button saveButton;
    protected Button cancelButton;
    protected SharePreferenceHelper sharePreferenceHelper;
    protected Button devButton;

    ///Activty to create a local profile to save a username
    ///ensuring others can't edit things

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Next lines assure activity uses the right language, otherwise some activities or fragment aren't fully catching up
        //Applying language start
        sharePreferenceHelper = new SharePreferenceHelper(this);
        String language = sharePreferenceHelper.languageReturn();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //Applying language end

        userEditText = findViewById(R.id.editUserName);
        userEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        passwordEditText = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.profile_saveButton);
        cancelButton = findViewById(R.id.profile_cancelButton);


        //adding this in here

        //permission check
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.VIBRATE,
                android.Manifest.permission.FOREGROUND_SERVICE,
                android.Manifest.permission.RECORD_AUDIO

        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        //to be taken out later
        devButton = findViewById(R.id.DevButton);

        devButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devbypass();
            }
        });

        //instantiating the sharepreferences helper
        sharePreferenceHelper = new SharePreferenceHelper(this);

        if(!(new SharePreferenceHelper(getApplicationContext()).getTutorialSeen())) {
            cancelButton.setVisibility(View.GONE);
            saveButton.setText("Continue");//bilingual
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String temp_name = userEditText.getText().toString();
                String temp_password = passwordEditText.getText().toString();
                if(temp_name.length()>1 && temp_password.length()>0){
                    sharePreferenceHelper.saveProfile(temp_name, temp_password);

                    // after entering information, go back into the main activity

                    if(!(new SharePreferenceHelper(getApplicationContext()).getTutorialSeen())) {
                        Intent intent = new Intent(getApplicationContext(), thresholdActivity.class );
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(profileActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                else Toast.makeText(getApplicationContext(), "Invalid Input !", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sharePreferenceHelper.passwordReturn().equals("it s null"))//TODO HAVE TO CHANGE SHAREDPREF
                    finish();
                else Toast.makeText(getApplicationContext(), "Create a profile first !", Toast.LENGTH_SHORT).show();

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void devbypass(){

        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        helper.setTutorialSeen(true);

        helper.saveProfile("Developer", Integer.toString(1));

        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());

        Person bob = new Person("bob", Integer.toString(9999999), 1, 1);
        dbhelper.insertPerson(bob);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class );
        startActivity(intent);

    }

}

