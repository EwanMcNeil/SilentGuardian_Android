package com.example.silentguardian_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class insertContactDialogFragment extends DialogFragment {

    protected EditText nameEditText;
    protected EditText numberEditText;
    protected Button saveButton;
    protected Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_contact, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        numberEditText = view.findViewById(R.id.numberEditText);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();

                if (!(name.equals(""))) {
                    DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
                    dbhelper.insertPerson(new Person(name, number));
                    //load the list view here
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
