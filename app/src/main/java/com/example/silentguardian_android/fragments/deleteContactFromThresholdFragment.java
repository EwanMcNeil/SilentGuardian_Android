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

public class deleteContactFromThresholdFragment extends DialogFragment {
    public static final String TAG = "__deleteContact";
    protected TextView nameFragmentTV;
    protected  TextView phoneFragmentTV;
    protected Button DeleteFromThresholdButton;
    protected  Button cancelButton;
    int selectedContactID;
    int thresholdVal = 0;

    ArrayList<Person> contactArrayList = new ArrayList<>();

    Person selectedPerson = new Person("Dummy", "5145555555");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_contact_from_threshold, container, false);

        nameFragmentTV = view.findViewById(R.id.nameDeleteThresholdFragmentTV);
        phoneFragmentTV = view.findViewById(R.id.phoneDeleteThresholdFragmentTV);
        DeleteFromThresholdButton = view.findViewById(R.id.deleteToThresholdFragmentButton);
        cancelButton = view.findViewById(R.id.cancelDeleteThresholdFragmentButton);

        Bundle bundle = getArguments();
        thresholdVal = bundle.getInt("ThresholdNumber");
        selectedContactID = bundle.getInt("contactSelectedName");
        Log.d("__Thres","Contact name received: "+ selectedContactID);

        DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
        List<Person> people = dbhelper.getAllPeople();


        for(int i = 0;i < people.size(); i++ ){
            Person temp;
            temp = people.get(i);
            contactArrayList.add(temp);
        }
        for(int j = 0;j < contactArrayList.size(); j++ ){
            if(contactArrayList.get(j).getID()== selectedContactID)
                selectedPerson = contactArrayList.get(j);
        }

        nameFragmentTV.setText("Name: " +selectedPerson.getName());
        phoneFragmentTV.setText("Phone Number: " +selectedPerson.getPhoneNumber() + "\n ThresholD Value:" + String.valueOf(thresholdVal));

        DeleteFromThresholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = selectedPerson.getName();
                String number = selectedPerson.getPhoneNumber();
                Log.d("__dbDebug","selected person id: " + selectedPerson.getID());
                Person tempPerson = new Person(null, null);
                //its making me intialize like this may cause issues
                if(thresholdVal == 1) {
                    tempPerson = new Person(selectedPerson.getID(), name, number, 0, selectedPerson.getThresholdTwo());
                } else if (thresholdVal == 2){
                    tempPerson = new Person(selectedPerson.getID(), name, number, selectedPerson.getThresholdOne(), 0);
                }
                else Log.d("__dbDebug","Threshold val has weird value");

                DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
                dbhelper.updatePerson(tempPerson);
                ((ThresholdSettingActivity)getActivity()).loadThresholdContactListView();
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

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();
        thresholdVal = bundle.getInt("ThresholdNumber");
        selectedContactID = bundle.getInt("contactSelected");
        Log.d("__Thres","Contact id received: "+ selectedContactID);

        DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
        List<Person> people = dbhelper.getAllPeople();


        for(int i = 0;i < people.size(); i++ ){
            Person temp;
            temp = people.get(i);
            contactArrayList.add(temp);
        }
        for(int j = 0;j < contactArrayList.size(); j++ ){
            if(contactArrayList.get(j).getID()==selectedContactID )
                selectedPerson = contactArrayList.get(j);
        }

        nameFragmentTV.setText("Name: " +selectedPerson.getName());
        phoneFragmentTV.setText("Phone Number: " +selectedPerson.getPhoneNumber() + "\n ThresholD Value:" + String.valueOf(thresholdVal));
    }

    @Override
    public void onPause() {
        super.onPause();
        Bundle bundle = getArguments();
        thresholdVal = bundle.getInt("ThresholdNumber");
        selectedContactID = bundle.getInt("contactSelected");
        Log.d("__Thres","Contact name received: "+ selectedContactID);

        DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
        List<Person> people = dbhelper.getAllPeople();


        for(int i = 0;i < people.size(); i++ ){
            Person temp;
            temp = people.get(i);
            contactArrayList.add(temp);
        }
        for(int j = 0;j < contactArrayList.size(); j++ ){
            if(contactArrayList.get(j).getID()==selectedContactID )
                selectedPerson = contactArrayList.get(j);
        }

        nameFragmentTV.setText("Name: " +selectedPerson.getName());
        phoneFragmentTV.setText("Phone Number: " +selectedPerson.getPhoneNumber() + "\n ThresholD Value:" + String.valueOf(thresholdVal));
    }
}
