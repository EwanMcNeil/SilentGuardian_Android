package com.example.silentguardian_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

public class profileActivity extends AppCompatActivity {

    protected EditText userEditText;
    protected EditText passwordEditText;
    protected Button saveButton;
    protected Button cancelButton;
    protected SharePreferenceHelper sharePreferenceHelper;


    ///Activty to create a local profile to save a username
    ///ensuring others can't edit things

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userEditText = findViewById(R.id.editUserName);
        userEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        passwordEditText = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        //instantiating the sharepreferences helper
        sharePreferenceHelper = new SharePreferenceHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String temp_name = userEditText.getText().toString();
                String temp_password = passwordEditText.getText().toString();
                if(temp_name.length()>1 && temp_password.length()>0){
                    sharePreferenceHelper.saveProfile(temp_name, temp_password);

                    // after entering information, go back into the main activity
                    Intent intent = new Intent(profileActivity.this, MainActivity.class);
                    startActivity(intent);
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

}

