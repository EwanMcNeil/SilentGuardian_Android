package com.example.silentguardian_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.MainActivity;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.messageGPSHelper;

import java.util.List;

public class sendMessageFragment extends DialogFragment {

    protected Button alertOneButton;
    protected Button alertTwoButton;
    private String[] thresholdOneNumbers;
    int oneSize;
    private String[] thresholdTwoNumbers;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sendmessage_fragment, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());



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