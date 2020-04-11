package com.example.silentguardian_android.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.silentguardian_android.Database.SharePreferenceHelper;
import com.example.silentguardian_android.ThresholdSettingActivity;
import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class insertContactDialogFragment extends DialogFragment {

    protected EditText nameEditText;
    protected EditText numberEditText;
    protected Button saveButton;
    protected Button cancelButton;
    protected SharePreferenceHelper sharePreferenceHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_contact, container, false);

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
        nameEditText = view.findViewById(R.id.nameEditText);
        numberEditText = view.findViewById(R.id.numberEditText);
        saveButton = view.findViewById(R.id.saveAddButton);
        cancelButton = view.findViewById(R.id.cancelAddButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();

                if (!(name.equals(""))) {
                    DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
                    dbhelper.insertPerson(new Person(name, number, 0, 0));
                    ((ThresholdSettingActivity)getActivity()).loadContactsListView();
                    getDialog().dismiss();

                }
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();

            }
        });


        return view;
    }
}
