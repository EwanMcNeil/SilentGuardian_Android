package com.example.silentguardian_android.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.R;

import java.util.Locale;

public class insertmessageFragment extends DialogFragment {

    private static final String TAG = "Threshold Message Check";

    protected EditText userDefinedThresholdMessage;
    protected Button saveMessage;
    protected Button cancelButton;
    protected SharePreferenceHelper sharePreferenceHelper;
    protected int ThresholdValue;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_user_defined_messages, container, false);


        //Next lines assure activity uses the right language, otherwise some activities or fragment aren't fully catching up
        //Applying language start
        sharePreferenceHelper = new SharePreferenceHelper(getContext());
        String language = sharePreferenceHelper.languageReturn();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        //Applying language end
        userDefinedThresholdMessage = view.findViewById(R.id.editUserDefinedMessageText);
        saveMessage = view.findViewById(R.id.saveMessageButton);
        cancelButton = view.findViewById(R.id.profile_cancelButton);

        sharePreferenceHelper = new SharePreferenceHelper(getContext());

        ThresholdValue = getArguments().getInt("threshold number");


        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp_message = userDefinedThresholdMessage.getText().toString();


                if (ThresholdValue == 1) {

                    sharePreferenceHelper.saveThresholdOneMessage(temp_message);
                    Toast.makeText(getContext(), "Threshold One's new message is: "+ sharePreferenceHelper.ThresholdOneMessageReturn(), Toast.LENGTH_LONG).show();

                } else {
                    sharePreferenceHelper.saveThresholdTwoMessage(temp_message);
                    Toast.makeText(getContext(), "Threshold Twos's new message is: " + sharePreferenceHelper.ThresholdTwoMessageReturn(), Toast.LENGTH_LONG).show();
                }


                //checking the logcat to see if the correct messages have been stored
                Log.d(TAG, "Threshold Message One = " + sharePreferenceHelper.ThresholdOneMessageReturn());
                Log.d(TAG, "Threshold Message Two = " + sharePreferenceHelper.ThresholdTwoMessageReturn());


                getDialog().dismiss();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return view;
    }
}
