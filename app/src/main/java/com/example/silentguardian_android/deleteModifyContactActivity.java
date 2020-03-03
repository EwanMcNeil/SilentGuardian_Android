package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;

import java.util.ArrayList;
import java.util.List;

public class deleteModifyContactActivity extends AppCompatActivity {
    protected EditText nameEditText;
    protected EditText numberEditText;
    protected Button deleteButton;
    protected Button cancelButton;
    protected Button saveChanges;
    int idContactDelete = 0;


    ArrayList<Person> contactArrayList = new ArrayList<>();
    Person selectedPerson = new Person("Dummy", "5145555555");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_modify_contact);

        nameEditText = findViewById(R.id.nameDeleteEditText);
        numberEditText = findViewById(R.id.phoneNumberET);
        deleteButton = findViewById(R.id.deleteButton);
        cancelButton = findViewById(R.id.cancelDeleteFragButton);
        saveChanges = findViewById(R.id.saveChangesButton);
        //get intent which contains the course ID
        Intent IDContactDelete =getIntent();
        idContactDelete = IDContactDelete.getIntExtra("IdContactToDelete", 0);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        List<Person> people = dbhelper.getAllPeople();

        for(int i = 0;i < people.size(); i++ ){
            Person temp;
            temp = people.get(i);
            contactArrayList.add(temp);
        }
        for(int j = 0;j < contactArrayList.size(); j++ ){
            if(contactArrayList.get(j).getID() == idContactDelete)
                selectedPerson = contactArrayList.get(j);
        }

        nameEditText.setText(selectedPerson.getName());
        numberEditText.setText(selectedPerson.getPhoneNumber());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbhelper = new DatabaseHelper(deleteModifyContactActivity.this);
                dbhelper.deletePerson(selectedPerson.getID());
                Intent intentDeletedContactToBubble = new Intent(deleteModifyContactActivity.this, BubbleActivity.class);
                startActivity(intentDeletedContactToBubble);
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();

                Person tempPerson = new Person(selectedPerson.getID(),name, number, selectedPerson.getThresholdOne(), selectedPerson.getThresholdTwo());


                if (!(name.equals(""))) {
                    DatabaseHelper dbhelper = new DatabaseHelper(deleteModifyContactActivity.this);
                    dbhelper.updatePerson(tempPerson);
                }
                Intent intentDeletedContactToBubble = new Intent(deleteModifyContactActivity.this, BubbleActivity.class);
                startActivity(intentDeletedContactToBubble);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDeletedContactToBubble = new Intent(deleteModifyContactActivity.this, BubbleActivity.class);
                startActivity(intentDeletedContactToBubble);

            }
        });

    }
}
