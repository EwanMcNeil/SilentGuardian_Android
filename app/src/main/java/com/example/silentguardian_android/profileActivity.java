package com.example.silentguardian_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

public class profileActivity extends AppCompatActivity {

    protected EditText userEditText;
    protected EditText passwordEditText;
    protected Button saveButton;
    protected Button cancelButton;
    protected SharePreferenceHelper sharePreferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userEditText = findViewById(R.id.editUserName);
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

                sharePreferenceHelper.saveProfile( temp_name,temp_password);

                // after entering information, go back into the main activity
                Intent intent = new Intent(profileActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
