package com.example.silentguardian_android.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.silentguardian_android.Helpers.DatabaseHelper;
import com.example.silentguardian_android.Helpers.Person;
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.Helpers.messageGPSHelper;
import com.example.silentguardian_android.R;

import java.util.List;
import java.util.Locale;

public class allClearActivity extends AppCompatActivity {

    private static final String TAG = "AllClearCheck";

    protected Button thresholdOneallClearButton;
    protected Button thresholdTwoallClearButton;
    protected messageGPSHelper SMSHelper;
    protected SharePreferenceHelper sharePreferenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_clear);

        SMSHelper = new messageGPSHelper(this);
        thresholdOneallClearButton = findViewById(R.id.AllClearButtonOne);
        thresholdTwoallClearButton = findViewById(R.id.thresholdTwoAllClearButton);
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

                    SMSHelper.sendAllClearMessage(temp,message);

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

                    SMSHelper.sendAllClearMessage(temp,message);

                    Log.d(TAG, "All Clear Message " + i + " has been sent. ");
                }

            }
        });



    }
}
