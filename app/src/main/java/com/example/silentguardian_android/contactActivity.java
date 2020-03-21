package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.fragments.insertContactDialogFragment;
import com.example.silentguardian_android.fragments.loadCellPhoneContact_fragment;

import java.util.ArrayList;
import java.util.List;

public class contactActivity extends AppCompatActivity {

    protected Button addContactButton;
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
        importContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(contactActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(contactActivity.this, "You have already granted this permission!", Toast.LENGTH_SHORT).show();
                    loadCellPhoneContact_fragment dialog = new loadCellPhoneContact_fragment();
                    dialog.show(getSupportFragmentManager(), "importAndroidContactFragment");
                } else {
                    ActivityCompat.requestPermissions(contactActivity.this, new String[] {Manifest.permission.READ_CONTACTS}, 3);
                }

            }
        });

        contactListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idToDelete = people.get(position).getID();
                Intent intentDeleteContact = new Intent(contactActivity.this, modifyContactActivity.class);
                intentDeleteContact.putExtra("IdContactToDelete", idToDelete);
                startActivity(intentDeleteContact);

            }
        });


    }



    public void loadContactsListView(){

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        people = dbhelper.getAllPeople();

        ArrayList<String> contactListText = new ArrayList<>();

        for(int i = 0;i < people.size(); i++ ){
            String temp = "";
            temp += people.get(i).getName() + '\n';
            temp += people.get(i).getPhoneNumber() + '\n';

            temp += "Threshold Two : " + people.get(i).getThresholdOne()+ '\n';
            temp += "Threshold One: " + people.get(i).getThresholdTwo();

            contactListText.add(temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contactListText);

        contactListview.setAdapter(arrayAdapter);

    }
}

