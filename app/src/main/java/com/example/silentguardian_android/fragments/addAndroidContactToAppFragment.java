package com.example.silentguardian_android.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.ThresholdSettingActivity;

import java.util.ArrayList;
import java.util.List;

public class addAndroidContactToAppFragment extends DialogFragment {
    protected TextView nameFragmentTV;
    protected  TextView phoneFragmentTV;
    protected Button addToContactButton;
    protected  Button cancelButton;
    protected String nameBundle;
    protected String phoneBundle;

    ArrayList<Person> contactArrayList = new ArrayList<>();
    Person selectedPerson = new Person("Dummy", "5145555555");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_cell_contact_to_app_fragment, container, false);

        nameFragmentTV = view.findViewById(R.id.androidNameTV);
        phoneFragmentTV = view.findViewById(R.id.androidPhoneNumberTV);
        addToContactButton = view.findViewById(R.id.addAndroidContactButton);
        cancelButton = view.findViewById(R.id.cancelAndroidButton);

        Bundle bundle = this.getArguments();
        nameBundle = bundle.getString("contactName");
        phoneBundle = bundle.getString("contactNumber");
        selectedPerson = new Person(nameBundle, phoneBundle,0,0);

        nameFragmentTV.setText("Name: " +selectedPerson.getName());
        phoneFragmentTV.setText("Phone Number: " +selectedPerson.getPhoneNumber());

        addToContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
                List<Person> currentContacts = dbhelper.getAllPeople();
                for(Person n : currentContacts) {
                    Log.d("__insertFrag",n.getName());
                    if (n.equals(selectedPerson)) {
                        Toast.makeText(getContext(), "Contact Already exists", Toast.LENGTH_LONG).show();
                        return;//do not insert
                    }
                }
                dbhelper.insertPerson(selectedPerson);
                Toast.makeText(getContext(), "Contact Added successfully!", Toast.LENGTH_LONG).show();
                ((ThresholdSettingActivity)getActivity()).loadContactsListView();
                getDialog().dismiss();

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
