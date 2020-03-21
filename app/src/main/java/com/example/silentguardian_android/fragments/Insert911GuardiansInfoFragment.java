package com.example.silentguardian_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.R;

public class Insert911GuardiansInfoFragment extends DialogFragment {


    protected Button closePopupButton;
    protected TextView policeGuardianInfo;
    protected TextView link;
    protected TextView note;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_helpful_info_for_911guardian, container, false);

        closePopupButton = view.findViewById(R.id.closeButton);
        policeGuardianInfo = view.findViewById(R.id.policeGuardianTextView);
        link = view.findViewById(R.id.linkTextView);
        note = view.findViewById(R.id.thresholdNoteTextView);


        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });



        return view;
    }
}
