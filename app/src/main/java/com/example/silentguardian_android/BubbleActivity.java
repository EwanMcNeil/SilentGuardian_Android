package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BubbleActivity extends AppCompatActivity {

    protected Button addContactButton;
    protected Button delContactButton;
    protected Button importContactsButton;
    protected ListView contactListview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        addContactButton = findViewById(R.id.addContactButton);
        delContactButton = findViewById(R.id.deleteContactButton);
        importContactsButton = findViewById(R.id.importContactButton);
        contactListview = findViewById(R.id.ContactListView);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertContactDialogFragment dialog = new insertContactDialogFragment();
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
            temp += people.get(i).getPhoneNumber();

            contactListText.add(temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contactListText);

        contactListview.setAdapter(arrayAdapter);




    }
}

