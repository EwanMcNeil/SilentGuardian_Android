package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;

import java.util.ArrayList;
import java.util.List;

public class AddContactToThreshload extends AppCompatActivity {

    TextView nameTextView;
    TextView numberTextView;
    protected Button addToThresholdButton;
    protected Button cancelButton;
    int selectedContactID = 0;
    int thresholdVal = 0;

    ArrayList<Person> contactArrayList = new ArrayList<>();
    Person selectedPerson = new Person("Dummy", "5145555555");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_to_threshload);

        nameTextView = findViewById(R.id.addNameThresholdTV);
        numberTextView = findViewById(R.id.phoneNumberThresholdEditText);


        

        addToThresholdButton = findViewById(R.id.addContactToThresholdButton);
        cancelButton = findViewById(R.id.cancelThresholdButton);
        selectedContactID = getIntent().getIntExtra("contactSelected",0);
        thresholdVal = getIntent().getIntExtra("ThresholdNumber", 0);



        DatabaseHelper dbhelper = new DatabaseHelper(this);
        List<Person> people = dbhelper.getAllPeople();


        for(int i = 0;i < people.size(); i++ ){
            Person temp;
            temp = people.get(i);
            contactArrayList.add(temp);
        }
        for(int j = 0;j < contactArrayList.size(); j++ ){
            if(contactArrayList.get(j).getID() == selectedContactID)
                selectedPerson = contactArrayList.get(j);
        }

        nameTextView.setText("Name: " +selectedPerson.getName());
        numberTextView.setText("Phone Number: " +selectedPerson.getPhoneNumber() + "\n ThresholD Value:" + String.valueOf(thresholdVal));



        addToThresholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = selectedPerson.getName();
                String number = selectedPerson.getPhoneNumber();


                Person tempPerson = new Person(null, null);
                //its making me intialize like this may cause issues
                if(thresholdVal == 1) {
                    tempPerson = new Person(selectedPerson.getID(), name, number, 1, selectedPerson.getThresholdTwo());
                } else if (thresholdVal == 2){
                    tempPerson = new Person(selectedPerson.getID(), name, number, selectedPerson.getThresholdOne(), 1);
                }

                DatabaseHelper dbhelper = new DatabaseHelper(AddContactToThreshload.this);
                dbhelper.updatePerson(tempPerson);

                Intent intentAddContactThreshold = new Intent(AddContactToThreshload.this, ThresholdSettingActivity.class);
                startActivity(intentAddContactThreshold);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentCancelAddContactThreshold = new Intent(AddContactToThreshload.this, ThresholdSettingActivity.class);
                intentCancelAddContactThreshold.putExtra("THRESHOLDVAL", thresholdVal);
                startActivity(intentCancelAddContactThreshold);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
