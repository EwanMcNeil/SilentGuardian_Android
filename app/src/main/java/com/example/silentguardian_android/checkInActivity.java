package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.silentguardian_android.fragments.TimePickerFragment;

import static android.Manifest.*;

public class checkInActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    protected TextView welcomeText;
    protected TextView explainAddressText;
    protected TextView EditaddressText;
    protected TextView setTimerText;
    protected TextView EdittimerText;
    protected Button saveAddressButton;
    protected Button startTimerButton;
    protected Button checkInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        welcomeText = findViewById(R.id.welcomeTextView);
        explainAddressText = findViewById(R.id.addressExplainTextView);
        EditaddressText = findViewById(R.id.EditaddressText);
        setTimerText = findViewById(R.id.setTimerTextView);
        EdittimerText = findViewById(R.id.EditiTimerText);
        saveAddressButton = findViewById(R.id.saveAddressButton);
        startTimerButton = findViewById(R.id.startTimerButton);
        checkInButton = findViewById(R.id. checkInButton);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Counter");

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Integer integerTime = intent.getIntExtra("TimeRemaining", 0);
                EdittimerText.setText(integerTime.toString());

            }
        };

        ////// dont forget to DE_REGISTER in the onDestory Method
        registerReceiver(broadcastReceiver, intentFilter);

        //asking user permission
        ActivityCompat.requestPermissions(this, new String[]{permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);

        //if i use the time picker make sure ane change this name to selectTimeButton or something
        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //working on clicking on clock instead of inputing time manually
               // DialogFragment timePicker = new TimePickerFragment();
               // timePicker.show(getSupportFragmentManager(),"Time Picker");


                Intent intent = new Intent(checkInActivity.this, CheckinService.class);
                Integer integerTimeSet = Integer.parseInt(EdittimerText.getText().toString());

               intent.putExtra("TimeValue", integerTimeSet );
               startService(intent);
                //startActivity(intent);
            }
        });

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
