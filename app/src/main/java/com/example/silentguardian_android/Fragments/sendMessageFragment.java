package com.example.silentguardian_android.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Helpers.DatabaseHelper;
import com.example.silentguardian_android.Helpers.Person;
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.Activities.MainActivity;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.Helpers.messageGPSHelper;

import java.util.List;
import java.util.Locale;

public class sendMessageFragment extends DialogFragment {

    protected Button alertOneButton;
    protected Button alertTwoButton;
    private String[] thresholdOneNumbers;
    int oneSize;
    private String[] thresholdTwoNumbers;
    protected SharePreferenceHelper sharePreferenceHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sendmessage_fragment, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
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



        List<Person> thresholdOnePeople;
        thresholdOnePeople = dbHelper.getThresholdOne();
        thresholdOneNumbers = new String[thresholdOnePeople.size()];

        for(int i = 0; i < thresholdOnePeople.size(); i++) {
            thresholdOneNumbers[i] = thresholdOnePeople.get(i).getPhoneNumber();
        }

        oneSize = thresholdOnePeople.size();
        alertOneButton = view.findViewById(R.id.alertOneButton);
        alertTwoButton = view.findViewById(R.id.alertTwoButton);

        alertOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageGPSHelper textHelper = new messageGPSHelper(getActivity());

                for(int i = 0; i < oneSize; i++) {
                    textHelper.sendMessage(thresholdOneNumbers[i], "test");
                }
                ((MainActivity)getActivity()).updateAllClearButton();
            }
        });



       // return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
}
