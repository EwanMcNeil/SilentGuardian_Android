package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;

import java.util.ArrayList;
import java.util.List;

public class BubbleActivity extends AppCompatActivity {

    protected Button addContactButton;
    protected Button delContactButton;
    protected Button importContactsButton;
    protected ListView contactListview;
    List<Person> people;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        addContactButton = findViewById(R.id.addContactButton);
        importContactsButton = findViewById(R.id.importContactButton);
        contactListview = findViewById(R.id.ContactListView);

        loadContactsListView();
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        people = dbhelper.getAllPeople();

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertContactDialogFragment dialog = new insertContactDialogFragment();
                dialog.show(getSupportFragmentManager(), "insertContactFragment");
            }
        });

        contactListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idToDelete = people.get(position).getID();
                Intent intentDeleteContact = new Intent(BubbleActivity.this, deleteModifyContactActivity.class);
                intentDeleteContact.putExtra("IdContactToDelete", idToDelete);
                startActivity(intentDeleteContact);

            }
        });

    }



    protected void loadContactsListView(){

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        people = dbhelper.getAllPeople();

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

        contactListview.setAdapter(arrayAdapter);

    }
}

