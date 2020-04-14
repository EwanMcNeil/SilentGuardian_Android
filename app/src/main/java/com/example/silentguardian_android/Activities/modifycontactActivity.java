package com.example.silentguardian_android.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.silentguardian_android.Helpers.DatabaseHelper;
import com.example.silentguardian_android.Helpers.Person;
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class modifycontactActivity extends AppCompatActivity {
    protected EditText nameEditText;
    protected EditText numberEditText;
    protected Button deleteButton;
    protected Button cancelButton;
    protected Button saveChanges;
    int idContactDelete = 0;
    protected SharePreferenceHelper sharePreferenceHelper;


    ArrayList<Person> contactArrayList = new ArrayList<>();
    Person selectedPerson = new Person("Dummy", "5145555555");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_modify_contact);
        //Next lines assure activity uses the right language, otherwise some activities or fragment aren't fully catching up
        //Applying language start
        sharePreferenceHelper = new SharePreferenceHelper(this);
        String language = sharePreferenceHelper.languageReturn();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //Applying language end

        nameEditText = findViewById(R.id.nameDeleteEditText);
        numberEditText = findViewById(R.id.phoneNumberET);
        deleteButton = findViewById(R.id.deleteButton);
        cancelButton = findViewById(R.id.cancelDeleteFragButton);
        saveChanges = findViewById(R.id.saveChangesButton);
        //get intent which contains the course ID
        Intent IDContactDelete = getIntent();
        idContactDelete = IDContactDelete.getIntExtra("IdContactToDelete", 0);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        List<Person> people = dbhelper.getAllPeople();

        for (int i = 0; i < people.size(); i++) {
            Person temp;
            temp = people.get(i);
            contactArrayList.add(temp);
        }
        for (int j = 0; j < contactArrayList.size(); j++) {
            if (contactArrayList.get(j).getID() == idContactDelete)
                selectedPerson = contactArrayList.get(j);
        }

        nameEditText.setText(selectedPerson.getName());
        numberEditText.setText(selectedPerson.getPhoneNumber());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbhelper = new DatabaseHelper(modifycontactActivity.this);
                dbhelper.deletePerson(selectedPerson.getID());
                //  Intent intentDeletedContactToBubble = new Intent(modifyContactActivity.this, contactActivity.class);
                // startActivity(intentDeletedContactToBubble);
                modifycontactActivity.this.finish();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();

                Person tempPerson = new Person(selectedPerson.getID(), name, number, selectedPerson.getThresholdOne(), selectedPerson.getThresholdTwo());


                if (!(name.equals(""))) {
                    DatabaseHelper dbhelper = new DatabaseHelper(modifycontactActivity.this);
                    dbhelper.updatePerson(tempPerson);
                } else {
                    Toast.makeText(modifycontactActivity.this, "Database not updated!", Toast.LENGTH_SHORT).show();
                }
                Intent intentDeletedContactToBubble = new Intent(modifycontactActivity.this, thresholdActivity.class);
                startActivity(intentDeletedContactToBubble);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDeletedContactToBubble = new Intent(modifycontactActivity.this, thresholdActivity.class);
                startActivity(intentDeletedContactToBubble);

            }
        });

    }
}
