package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.fragments.Insert911GuardiansInfoFragment;
import com.example.silentguardian_android.fragments.InsertThresholdMessageDialogFragment;
import com.example.silentguardian_android.fragments.deleteContactFromThresholdFragment;
import com.example.silentguardian_android.fragments.insertContactDialogFragment;
import com.example.silentguardian_android.fragments.loadCellPhoneContact_fragment;
import com.example.silentguardian_android.fragments.setContactToThresholdFragment;

import java.util.ArrayList;
import java.util.List;

public class ThresholdSettingActivity extends AppCompatActivity {

    private static final String TAG = "AllClearCheck";

    protected TextView thresholdTextview;

    //thresholdVal is either One or Two depending on the droplink

    protected int thresholdVal;
    protected ListView wholeContactsListView;
    protected ListView threshHoldContactsListView;
    protected List<Person> mainList = null;
    protected Button add911GuardianButton;

    //commenting out for now: MOVED TO MAIN ACTIVITY
    //protected Button allClearButton;
    //protected Button thresholdTwoAllClearButton;
    protected ArrayList<Person> thresholdList = new ArrayList<>();

    protected Button defineMessageButton;

    //from contact activity
    protected Button addContactButton;
    protected Button importContactsButton;

    protected Boolean contactMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_threshold_setting);



        //from contact Actvitity
        addContactButton = findViewById(R.id.freshAddContactButton);
        importContactsButton = findViewById(R.id.importContactButton);


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

                if (ContextCompat.checkSelfPermission(ThresholdSettingActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(contactActivity.this, "You have already granted this permission!", Toast.LENGTH_SHORT).show();
                    loadCellPhoneContact_fragment dialog = new loadCellPhoneContact_fragment();
                    dialog.show(getSupportFragmentManager(), "importAndroidContactFragment");
                } else {
                    ActivityCompat.requestPermissions(ThresholdSettingActivity.this, new String[] {Manifest.permission.READ_CONTACTS}, 3);
                }

            }
        });


        thresholdTextview = findViewById(R.id.thresholdTextview);
        thresholdVal = 1;

        wholeContactsListView = findViewById(R.id.wholecontactsListView);
        threshHoldContactsListView = findViewById(R.id.thresholdContactsListView);

        //defining all clear button functionality


        add911GuardianButton = findViewById(R.id.add911Button);



        defineMessageButton = findViewById(R.id.defineMessageButton);

        defineMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //instantiating fragment code
                InsertThresholdMessageDialogFragment dialog = new InsertThresholdMessageDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("threshold number", thresholdVal);

                dialog.setArguments(bundle);

                dialog.show(getSupportFragmentManager(), "InsertThresholdMessageFragment");
            }
        });




        loadThresholdContactListView();


        defineMessageButton.setVisibility(View.GONE);
        add911GuardianButton.setVisibility(View.GONE);
        threshHoldContactsListView.setVisibility(View.GONE);
        contactMode = true;

    }

    @Override
    protected void onStart() {
        super.onStart();
        thresholdTextview.setText("Click the three dots!");//reseting textview
        //on start these buttons should be gone



        //thresholdVal = 1;  //threshold value is defaulted to One
        loadContactsListView();
        loadThresholdContactListView();
        wholeContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(contactMode == false) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("contactSelected", mainList.get(position).getID());
                    bundle.putInt("ThresholdNumber", thresholdVal);
                    //TODO DELETE setContactToThresholdFragment
                    /*setContactToThresholdFragment dialog = new setContactToThresholdFragment();
                   dialog.setArguments(bundle);
                   dialog.show(getSupportFragmentManager(), "insertContactFragment");*/

                    //directly add to guardians, it s more intuitive,
                    Person selectedPerson = mainList.get(position);
                    String name = selectedPerson.getName();
                    String number = selectedPerson.getPhoneNumber();

                    Person tempPerson = new Person(null, null);
                    //its making me intialize like this may cause issues
                    if(thresholdVal == 1) {
                        tempPerson = new Person(selectedPerson.getID(), name, number, 1, selectedPerson.getThresholdTwo());
                    } else if (thresholdVal == 2){
                        tempPerson = new Person(selectedPerson.getID(), name, number, selectedPerson.getThresholdOne(), 1);
                    }

                    DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                    dbhelper.updatePerson(tempPerson);
                    loadThresholdContactListView();

                }
                else{
                    Intent intent = new Intent(ThresholdSettingActivity.this, modifyContactActivity.class);
                    intent.putExtra("IdContactToDelete",mainList.get(position).getID());
                    startActivity(intent);
                }
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


        //defining all clear button functionality


        add911GuardianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Insert911GuardiansInfoFragment dialog = new Insert911GuardiansInfoFragment();

                dialog.show(getSupportFragmentManager(), "Insert911GuardiansInfoFragment");

                //not actually putting 911 in the number for obvious reasons: can mention this to prof
                //for demo, put one of the teams numbers to show it will work
                DatabaseHelper dbhelper = new DatabaseHelper(getBaseContext());
                dbhelper.insertPerson(new Person("Police", "123", 0, 1));
                Log.d(TAG, "police contact has been placed ");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        thresholdTextview.setText("Click the three dots!");//reseting textview
    }

    public void loadContactsListView() {

            DatabaseHelper dbhelper = new DatabaseHelper(this);
            List<Person> people = dbhelper.getAllPeople();
            mainList = people;
            ArrayList<String> contactListText = new ArrayList<>();

            for (int i = 0; i < people.size(); i++) {
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


        public void loadThresholdContactListView () {
            DatabaseHelper dbhelper = new DatabaseHelper(this);
            List<Person> people = dbhelper.getAllPeople();
          //  mainList = people;

            ArrayList<String> contactListText = new ArrayList<>();

            //ArrayList<Person> tempContactLost = new ArrayList<>();

            for (int i = 0; i < people.size(); i++) {
                String temp = "";
                Person tempPerson = new Person(people.get(i).getID(), people.get(i).getName(), people.get(i).getPhoneNumber(), people.get(i).getThresholdOne(), people.get(i).getThresholdTwo());
                temp += people.get(i).getName() + '\n';
                temp += people.get(i).getPhoneNumber() + '\n';

                if (thresholdVal == 1) {
                    if (people.get(i).getThresholdOne() == 1) //because threshold One is set to One if they are added to list(boolean but int)
                    {
                        contactListText.add(temp);
                        thresholdList.add(tempPerson);
                    }
                } else if (thresholdVal == 2) {
                    if (people.get(i).getThresholdTwo() == 1) {
                        contactListText.add(temp);
                        thresholdList.add(tempPerson);
                    }
                }

            }

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactListText);
            threshHoldContactsListView.setAdapter(arrayAdapter);
        }


    ///code for the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.thresholdmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem changeView = menu.findItem(R.id.editmodedropdown);
        MenuItem changeThres = menu.findItem(R.id.changeThresholddropdown);
        if(contactMode){
            changeView.setTitle("Edit your Guardians");
            changeThres.setVisible(false);
            if(thresholdVal ==1)
                changeThres.setTitle("Edit Level 2");
            else changeThres.setTitle("Edit Level 1");
        }
        else {
            changeView.setTitle("Add Contacts");
            changeThres.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.editmodedropdown:
                if(contactMode){

                    loadContactsListView();
                    thresholdVal = 1;
                    defineMessageButton.setVisibility(View.VISIBLE);
                    add911GuardianButton.setVisibility(View.VISIBLE);
                    thresholdTextview.setVisibility(View.VISIBLE);
                    String currentThres = "Guardians Level " + thresholdVal;
                    thresholdTextview.setText(currentThres);
                    thresholdTextview.setBackground(null);
                    threshHoldContactsListView.setVisibility(View.VISIBLE);
                    contactMode =false;
                    addContactButton.setVisibility(View.GONE);
                   importContactsButton.setVisibility(View.GONE);


                }
                else{
                    defineMessageButton.setVisibility(View.GONE);
                    add911GuardianButton.setVisibility(View.GONE);
                    thresholdTextview.setText("Click the three dots!");//reseting textview
                    threshHoldContactsListView.setVisibility(View.GONE);
                    contactMode = true;
                    addContactButton.setVisibility(View.VISIBLE);
                    importContactsButton.setVisibility(View.VISIBLE);


                }
                return true;
            case R.id.changeThresholddropdown:
                //update textview

                if (thresholdVal == 1) {
                    thresholdVal = 2;
                    item.setTitle("Edit Level 1");
                } else if (thresholdVal == 2){
                    thresholdVal = 1;
                    item.setTitle("Edit Level 2");
                }

                String currentThres = "Guardians Level " + thresholdVal;
                thresholdTextview.setText(currentThres);


                loadThresholdContactListView();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    }
