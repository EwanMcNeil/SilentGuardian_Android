package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

import static android.Manifest.*;

public class checkInActivity extends AppCompatActivity {

    protected TextView welcomeText;
    protected TextView explainAddressText;
    protected TextView EditaddressText;
    protected TextView setTimerText;
    protected TextView secondEditText;
    protected TextView minuteEditText;
    protected TextView hourEditText;
    protected TextView secondTextView;
    protected TextView minuteTextView;
    protected TextView hourTextView;

    protected Button saveAddressButton;
    protected Button startTimerButton;
    protected Button checkInButton;
    protected Button restartButton;

    protected SharePreferenceHelper sharePreferenceHelper;

    protected Integer TimerStartClicked;
    protected Integer Hours;
    protected Integer Minutes;
    protected Integer Seconds;

    protected Integer miliHours;
    protected Integer miliMinutes;

    private static final String TAG = "CheckIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        welcomeText = findViewById(R.id.welcomeTextView);
        explainAddressText = findViewById(R.id.addressExplainTextView);
        EditaddressText = findViewById(R.id.EditaddressText);
        setTimerText = findViewById(R.id.setTimerTextView);
        saveAddressButton = findViewById(R.id.saveAddressButton);
        startTimerButton = findViewById(R.id.startTimerButton);
        checkInButton = findViewById(R.id. checkInButton);
        restartButton = findViewById(R.id.restartButton);

        secondEditText = findViewById(R.id.secondsEditText);
        minuteEditText = findViewById(R.id.minutesEditText);
        hourEditText = findViewById(R.id.hourEditText);

        secondTextView = findViewById(R.id.secondsTextView);
        minuteTextView = findViewById(R.id.minuteTextView);
        hourTextView = findViewById(R.id.hourTextView);

        sharePreferenceHelper = new SharePreferenceHelper(this);







        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Counter");

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Integer Hours = (intent.getIntExtra("TimeRemaining", 0)) / 3600;
                hourEditText.setText(Hours.toString());
                Log.d(TAG, "Checking  final Hours = " + Hours);

                Integer Minutes =( ((intent.getIntExtra("TimeRemaining", 0)) -(Hours * 3600)) / 60);
                minuteEditText.setText(Minutes.toString());
                Log.d(TAG, "Checking  final Minutes = " + Minutes);

                Integer Seconds = ((intent.getIntExtra("TimeRemaining", 0)) -(Hours * 3600) - (Minutes * 60));
                secondEditText.setText(Seconds.toString());
                Log.d(TAG, "Checking  final Seconds = " + Seconds);

                // this is where i need to add the other numbers as well
                //Integer secondsIntegerTime = intent.getIntExtra("TimeRemaining", 0);
                //secondEditText.setText(secondsIntegerTime.toString());


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

               // Hours = hourEditText.getText().toString();
               // Minutes = minuteEditText.getText().toString();
               // Seconds = secondEditText.getText().toString();

               // sharePreferenceHelper.saveTime(Hours ,Minutes,Seconds);
                //TimerStartClicked = 1;

                //working on clicking on clock instead of inputing time manually
                //DialogFragment timePicker = new TimePickerFragment();
               //timePicker.show(getSupportFragmentManager(),"Time Picker");


                // this is where i need to add the other numbers as well, commenting this for now
               // Intent intent = new Intent(checkInActivity.this, CheckinService.class);

                Hours = Integer.parseInt(hourEditText.getText().toString());
                Minutes = Integer.parseInt(minuteEditText.getText().toString());
                Seconds = Integer.parseInt(secondEditText.getText().toString());

                Log.d(TAG, "Checking units, hours = " + Hours + ", Minutes = " + Minutes + ", Seconds = " + Seconds);
                miliHours = Hours * 3600;
                miliMinutes = Minutes * 60;
                Log.d(TAG, "Checking units, milihours = " + miliHours + ", miliMinutes = " + miliMinutes );

                Integer secondIntegerTimeSet = Seconds + miliMinutes + miliHours;
                Log.d(TAG, "Total seconds  = " + secondIntegerTimeSet );
                //Integer secondIntegerTimeSet = Seconds;


///////////////////////// this line needs to be uncommented in order to send the seconds
              //Integer secondIntegerTimeSet = Integer.parseInt(secondEditText.getText().toString());

                //Integer minuteIntegerTimeSet = Integer.parseInt(minuteEditText.getText().toString());
               // Integer hourIntegerTimeSet = Integer.parseInt(hourEditText.getText().toString());


                //intent.putExtra("secondTimeValue", secondIntegerTimeSet );
                //intent.putExtra("minuteTimeValue", minuteIntegerTimeSet );
                //intent.putExtra("hourTimeValue", hourIntegerTimeSet );


                Intent intent = new Intent(checkInActivity.this, CheckinService.class);
                Bundle extras = new Bundle();

                //extras.putInt("TimerStartClicked",TimerStartClicked);

                extras.putInt("secondTimeValue",secondIntegerTimeSet);
                //extras.putInt("minuteTimeValue",minuteIntegerTimeSet);
                //extras.putInt("hourTimeValue",hourIntegerTimeSet);
                intent.putExtras(extras);


                startService(intent);
                //startActivity(intent);
            }
        });

    }

/*
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

 */


}
