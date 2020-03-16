package com.example.silentguardian_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;

import java.util.List;

public class allClearActivity extends AppCompatActivity {

    private static final String TAG = "AllClearCheck";

    protected Button thresholdOneallClearButton;
    protected Button thresholdTwoallClearButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thresholdOneallClearButton = findViewById(R.id.AllClearButtonOne);
        thresholdTwoallClearButton = findViewById(R.id.thresholdTwoAllClearButton);


/*
        thresholdOneallClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //creating db object to use the functions
                DatabaseHelper dbhelper = new DatabaseHelper(getBaseContext());
                List<Person> people = dbhelper.getThresholdOne();


                ////create loop that will message the "I am Safe" message to all guardians who are in threshold one
                for(int i = 0;i < people.size(); i++ ){

                    String temp = " ";

                    temp = people.get(i).getPhoneNumber();

                    String message ="I am safe, all clear ";

                    messageGPSHelper.sendAllClearMessage(temp,message);

                    Log.d(TAG, "All Clear Message " + i + " has been sent. ");
                }


            }
        });

        thresholdTwoallClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //creating db object to use the functions
                DatabaseHelper dbhelper = new DatabaseHelper(getBaseContext());
                List<Person> people = dbhelper.getThresholdTwo();


                ////create loop that will message the "I am Safe" message to all guardians who are in threshold two
                for(int i = 0;i < people.size(); i++ ){

                    String temp = " ";

                    temp = people.get(i).getPhoneNumber();

                    String message ="I am safe, all clear ";

                    messageGPSHelper.sendAllClearMessage(temp,message);

                    Log.d(TAG, "All Clear Message " + i + " has been sent. ");
                }

            }
        });

 */

    }
}
