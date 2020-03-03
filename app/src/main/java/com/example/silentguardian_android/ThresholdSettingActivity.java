package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThresholdSettingActivity extends AppCompatActivity {

    protected TextView thresholdEditText;

    //thresholdVal is either One or Two depending on the droplink

    protected int thresholdVal;
    protected Button changeThresholdButton;
    protected ListView wholeContactsListView;
    protected ListView threshHoldContactsListView;
    protected List<Person> mainList = null;

    protected ArrayList<Person> thresholdList = new ArrayList<>();

    protected Button defineMessageButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_threshold_setting);
        thresholdEditText = findViewById(R.id.thresholdEditText);
        thresholdVal = getIntent().getIntExtra("THRESHOLDVAL", 0);

        wholeContactsListView = findViewById(R.id.wholecontactsListView);
        threshHoldContactsListView = findViewById(R.id.thresholdContactsListView);


        defineMessageButton = findViewById(R.id.defineMessageButton);

        defineMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //instantiating fragment code
                InsertThresholdMessageDialogFragment dialog = new InsertThresholdMessageDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("threshold number", thresholdVal );

                dialog.setArguments(bundle);

                dialog.show(getSupportFragmentManager(),"InsertThresholdMessageFragment");
            }
        });


        changeThresholdButton = findViewById(R.id.changeThresholdButton);

        loadContactsListView();
        loadThresholdContactListView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //thresholdVal = 1;  //threshold value is defaulted to One
        loadContactsListView();
        loadThresholdContactListView();

        changeThresholdButton.setText("Current Threshold is: " + thresholdVal + '\n' +"Click to Change threshold");

        changeThresholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thresholdVal == 1){
                    thresholdVal =2;
                }else{
                    thresholdVal =1;
                }

                finish();
                overridePendingTransition(0, 0);
                Intent intent = new Intent(ThresholdSettingActivity.this, ThresholdSettingActivity.class);
                intent.putExtra("THRESHOLDVAL", thresholdVal);
                startActivity(intent);
                overridePendingTransition(0, 0);

                changeThresholdButton.setText("Current Threshold is: " + thresholdVal + " Click to Change threshold");

            }
          
        });
        thresholdEditText.setText("Contacts in Threshold:" + thresholdVal);
        wholeContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent selectedContactIntent = new Intent(ThresholdSettingActivity.this, AddContactToThreshload.class);
                //selectedContactIntent.putExtra("contactSelected", mainList.get(position).getID());
                //selectedContactIntent.putExtra("ThresholdNumber", thresholdVal); //sends either a one or a two to the next activity
                //startActivity(selectedContactIntent);


                Bundle bundle = new Bundle();
                bundle.putInt("contactSelected", mainList.get(position).getID());
                bundle.putInt("ThresholdNumber", thresholdVal);
                setContactToThresholdFragment dialog = new setContactToThresholdFragment();
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "insertContactFragment");
            }
        });

        threshHoldContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("contactSelected", thresholdList.get(position).getID());
                bundle.putInt("ThresholdNumber", thresholdVal);
                deleteContactFromThresholdFragment dialog = new deleteContactFromThresholdFragment();
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "insertContactFragment");
            }
        });
    }




    protected void loadContactsListView(){

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        List<Person> people = dbhelper.getAllPeople();

        ArrayList<String> contactListText = new ArrayList<>();

        for(int i = 0;i < people.size(); i++ ){
            String temp = "";
            temp += people.get(i).getName() + '\n';
            temp += people.get(i).getPhoneNumber() + '\n';

            //dont think we need to display the threshold values
            //or maybe we do
            //temp += "Threshold: " + people.get(i).getThreshold();

            //we don't need it, I had issues when I was implementing the listviews and going through activities so I wanted to make
            //sure I had the right threshold in each activity

            contactListText.add(temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contactListText);

        wholeContactsListView.setAdapter(arrayAdapter);

    }
    protected void loadThresholdContactListView(){
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        List<Person> people = dbhelper.getAllPeople();
        mainList = people;

        ArrayList<String> contactListText = new ArrayList<>();
        //ArrayList<Person> tempContactLost = new ArrayList<>();

        for(int i = 0;i < people.size(); i++ ){
            String temp = "";
            Person tempPerson = new Person(people.get(i).getID(),people.get(i).getName(),people.get(i).getPhoneNumber(),people.get(i).getThresholdOne(),people.get(i).getThresholdTwo());
            temp += people.get(i).getName() + '\n';
            temp += people.get(i).getPhoneNumber() + '\n';

            if(thresholdVal == 1){
                if(people.get(i).getThresholdOne()== 1) //because threshold One is set to One if they are added to list(boolean but int)
                {
                    contactListText.add(temp);
                    thresholdList.add(tempPerson);
                }
            }else if (thresholdVal == 2) {
                if (people.get(i).getThresholdTwo() == 1)
                {
                    contactListText.add(temp);
                    thresholdList.add(tempPerson);
                }
            }
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactListText);
        threshHoldContactsListView.setAdapter(arrayAdapter);
    }
}
