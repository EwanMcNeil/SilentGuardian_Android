package com.example.silentguardian_android.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.R;

import java.util.Locale;

public class Insert911GuardiansInfoFragment extends DialogFragment {


    protected Button closePopupButton;
    protected TextView policeGuardianInfo;
    protected TextView link;
    protected TextView note;
    protected SharePreferenceHelper sharePreferenceHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_helpful_info_for_911guardian, container, false);
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
