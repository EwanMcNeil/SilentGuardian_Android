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
    protected int thresholdVal;
    protected ListView wholeContactsListView;
    protected ListView threshHoldContactsListView;
    protected List<Person> mainList = null;
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

                dialog.show(getSupportFragmentManager(),"InsertThresholdMessageFragment");
            }
        });

        loadContactsListView();
        loadThresholdContactListView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        thresholdVal = getIntent().getIntExtra("THRESHOLDVAL", 0);
        loadContactsListView();
        loadThresholdContactListView();

        thresholdEditText.setText("Contacts in Threshold:" + thresholdVal);
        wholeContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent selectedContactIntent = new Intent(ThresholdSettingActivity.this, AddContactToThreshload.class);
                selectedContactIntent.putExtra("contactSelected", mainList.get(position).getID());
                selectedContactIntent.putExtra("ThresholdNumber", thresholdVal);
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
            temp += "Threshold: " + people.get(i).getThreshold();

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
            temp += "Threshold: " + people.get(i).getThreshold();
            if(people.get(i).getThreshold()== thresholdVal)
                contactListText.add(temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactListText);
        threshHoldContactsListView.setAdapter(arrayAdapter);
    }
}
