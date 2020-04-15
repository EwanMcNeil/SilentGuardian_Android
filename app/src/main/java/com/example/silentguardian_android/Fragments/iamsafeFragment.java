package com.example.silentguardian_android.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Activities.mainActivity;
import com.example.silentguardian_android.Helpers.DatabaseHelper;
import com.example.silentguardian_android.Helpers.Person;
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.Helpers.messageGPSHelper;
import com.example.silentguardian_android.R;

import java.util.List;
import java.util.Locale;

public class iamsafeFragment extends DialogFragment {


    private static final String TAG = "AllClearCheck";

    protected Button thresholdOneallClearButton;
    protected Button thresholdTwoallClearButton;
    protected messageGPSHelper SMSHelper;
    protected SharePreferenceHelper sharePreferenceHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.safe_fragment, container, false);


        SMSHelper = new messageGPSHelper(getContext());
        thresholdOneallClearButton = view.findViewById(R.id.safebutton1frag);
        thresholdTwoallClearButton = view.findViewById(R.id.safebutton2frag);
        //Next lines assure activity uses the right language, otherwise some activities or fragment aren't fully catching up
        //Applying language start
        sharePreferenceHelper = new SharePreferenceHelper(getContext());
        String language = sharePreferenceHelper.languageReturn();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
        //Applying language end


        thresholdOneallClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating db object to use the functions
                DatabaseHelper dbhelper = new DatabaseHelper(getContext());
                List<Person> people = dbhelper.getThresholdOne();


                ////create loop that will message the "I am Safe" message to all guardians who are in threshold one
                for (int i = 0; i < people.size(); i++) {

                    String temp = " ";

                    temp = people.get(i).getPhoneNumber();

                    String message = "I am safe, all clear ";

                    SMSHelper.sendAllClearMessage(temp, message);

                    Log.d(TAG, "All Clear Message " + i + " has been sent. ");
                }
                ((mainActivity)getActivity()).updateAllClearButton();
              dismiss();
            }
        });

        thresholdTwoallClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating db object to use the functions
                DatabaseHelper dbhelper = new DatabaseHelper(getContext());
                List<Person> people = dbhelper.getThresholdTwo();


                ////create loop that will message the "I am Safe" message to all guardians who are in threshold two
                for (int i = 0; i < people.size(); i++) {

                    String temp = " ";

                    temp = people.get(i).getPhoneNumber();

                    String message = "I am safe, all clear ";

                    SMSHelper.sendAllClearMessage(temp, message);

                    Log.d(TAG, "All Clear Message " + i + " has been sent. ");
                }

                ((mainActivity)getActivity()).updateAllClearButton();
              dismiss();

            }
        });


        return view;
    }



}
