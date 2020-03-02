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
import java.util.List;

public class ThresholdSettingActivity extends AppCompatActivity {

    protected TextView thresholdEditText;

    //thresholdVal is either One or Two depending on the droplink

    protected int thresholdVal;
    protected Button changeThresholdButton;
    protected ListView wholeContactsListView;
    protected ListView threshHoldContactsListView;
    protected List<Person> mainList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_threshold_setting);
        thresholdEditText = findViewById(R.id.thresholdEditText);
        thresholdVal = getIntent().getIntExtra("THRESHOLDVAL", 0);

        wholeContactsListView = findViewById(R.id.wholecontactsListView);
        threshHoldContactsListView = findViewById(R.id.thresholdContactsListView);
        changeThresholdButton = findViewById(R.id.changeThresholdButton);
        loadContactsListView();
        loadThresholdContactListView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        thresholdVal = 1;  //threshold value is defaulted to One
        loadContactsListView();
        loadThresholdContactListView();

        changeThresholdButton.setText("Current Threshold is:" + thresholdVal + "Click to Change threshold");
        changeThresholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thresholdVal == 1){
                    thresholdVal =2;
                }else{
                    thresholdVal =1;
                }
            }
        });
        thresholdEditText.setText("Contacts in Threshold:" + thresholdVal);
        wholeContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent selectedContactIntent = new Intent(ThresholdSettingActivity.this, AddContactToThreshload.class);
                selectedContactIntent.putExtra("contactSelected", mainList.get(position).getID());
                selectedContactIntent.putExtra("ThresholdNumber", thresholdVal); //sends either a one or a two to the next activity
                startActivity(selectedContactIntent);
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

        for(int i = 0;i < people.size(); i++ ){
            String temp = "";
            temp += people.get(i).getName() + '\n';
            temp += people.get(i).getPhoneNumber() + '\n';

           // temp += "Threshold: " + people.get(i).getThreshold();
            //redundant line

            if(thresholdVal == 1){
                if(people.get(i).getThresholdOne()== 1) //because threshold One is set to One if they are added to list(boolean but int)
                    contactListText.add(temp);
            }else if (thresholdVal == 2) {
                if (people.get(i).getThresholdTwo() == 1)
                    contactListText.add(temp);
            }
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactListText);
        threshHoldContactsListView.setAdapter(arrayAdapter);
    }
}
