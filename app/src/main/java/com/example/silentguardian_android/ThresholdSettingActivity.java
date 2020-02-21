package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold_setting);
        thresholdEditText = findViewById(R.id.thresholdEditText);
        thresholdVal = getIntent().getIntExtra("THRESHOLDVAL", 0);

        wholeContactsListView = findViewById(R.id.wholecontactsListView);
        threshHoldContactsListView = findViewById(R.id.thresholdContactsListView);

        loadContactsListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        thresholdVal = getIntent().getIntExtra("THRESHOLDVAL", 0);

        thresholdEditText.setText("Contacts in Threshold:" + thresholdVal);
    }

    protected void loadContactsListView(){

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        List<Person> people = dbhelper.getAllPeople();

        ArrayList<String> contactListText = new ArrayList<>();

        for(int i = 0;i < people.size(); i++ ){
            String temp = "";
            temp += people.get(i).getName() + '\n';
            temp += people.get(i).getPhoneNumber();

            contactListText.add(temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contactListText);

        wholeContactsListView.setAdapter(arrayAdapter);

    }
}
