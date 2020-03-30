package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.Database.SharePreferenceHelper;

import java.util.List;

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
    protected Boolean TimerRunning = false;
    protected Boolean userTimerDone=false;
    protected Boolean iAmSafe=false;
    protected Boolean resetValue = false;

    protected messageGPSHelper SMSHelper;

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


        sharePreferenceHelper = new SharePreferenceHelper(this);

        SMSHelper = new messageGPSHelper(this);







        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Counter");

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                if(resetValue ==true)
                {
                    hourEditText.setText(null);
                    minuteEditText.setText(null);
                    secondEditText.setText(null);
                    context.stopService(intent);
                }



                Integer Hours = (intent.getIntExtra("TimeRemaining", 0)) / 3600;
                hourEditText.setText(Hours.toString() + "   :");
                Log.d(TAG, "Checking  final Hours = " + Hours);

                Integer Minutes =( ((intent.getIntExtra("TimeRemaining", 0)) -(Hours * 3600)) / 60);
                minuteEditText.setText(Minutes.toString()+ "   :");
                Log.d(TAG, "Checking  final Minutes = " + Minutes);

                Integer Seconds = ((intent.getIntExtra("TimeRemaining", 0)) -(Hours * 3600) - (Minutes * 60));
                secondEditText.setText(Seconds.toString());
                Log.d(TAG, "Checking  final Seconds = " + Seconds);

                if(Hours==0 & Minutes==0 & Seconds==0 & userTimerDone==true)
                {
                    Log.d(TAG, "Entered the last loop ");

                    if(iAmSafe == false)
                    {
                        //creating db object to use the functions
                        DatabaseHelper dbhelper = new DatabaseHelper(getBaseContext());
                        List<Person> people = dbhelper.getThresholdOne();

                       // if (!sharePreferenceHelper.returnCheckInAddress().toString().matches(""))
                        //{


                        Log.d(TAG, "texting address loop ");
                        //}
                        ////create loop that will message the "I am Safe" message to all guardians who are in threshold one
                        for(int i = 0;i < people.size(); i++ ){

                            String temp = " ";

                            temp = people.get(i).getPhoneNumber();

                            String message ="Please call or text me, I have missed my Check-in with Silent Guardians, I was heading to this location: " + sharePreferenceHelper.returnCheckInAddress() +".";

                            //change this back to sendMessage after testing
                            SMSHelper.sendMessage(temp,message);

                            Log.d(TAG, "Missed Check-in messages " + i + " has been sent. ");
                            hourEditText.setText("");//TODO used to crash because there was no reset of the editTExt
                            minuteEditText.setText("");
                            secondEditText.setText("");


                        }
                    }

                    //not sure if i need an else here;

                }


                //this is where i will set a function to check if the user timer has gone off
                //if it has, i still start a new timer that will be 5-10 mins, if they fail to hit the i am safe within this clock, the message will
                //send out.
                if( Hours==0 & Minutes==0 & Seconds==0 & userTimerDone==false)//hum
                {
                    //remember to make in onDestroy to clear this
                    userTimerDone = true;

                    Log.d(TAG, "User has 5 mins to hit i am safe button " );

                    finalTimer();

                }


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

                //at some point add a boolean to see if timer is runnning, if it is, change the text on the button to "reset" or something
                TimerRunning = true;
                restartButton.setVisibility(View.VISIBLE);



                // Hours = hourEditText.getText().toString();
                // Minutes = minuteEditText.getText().toString();
                // Seconds = secondEditText.getText().toString();



                // this is where i need to add the other numbers as well, commenting this for now
                // Intent intent = new Intent(checkInActivity.this, CheckinService.class);


                if (!hourEditText.getText().toString().matches("")//at least one must be good
                    || !minuteEditText.getText().toString().matches("")
                    || !secondEditText.getText().toString().matches(""))
                {//dont really need the main check, it s just send a toast user presses start timer without indicating a time

                    //set to zero when nothing was written
                    Hours = hourEditText.getText().toString().matches("") ? 0 : Integer.parseInt(hourEditText.getText().toString());
                    Minutes = minuteEditText.getText().toString().matches("") ? 0 : Integer.parseInt(minuteEditText.getText().toString());
                    Seconds = secondEditText.getText().toString().matches("") ? 0 : Integer.parseInt(secondEditText.getText().toString());

                    Integer secondIntegerTimeSet = 0;

                    Log.d(TAG, "Checking units, hours = " + Hours + ", Minutes = " + Minutes + ", Seconds = " + Seconds);


                    miliHours = Hours * 3600;
                    miliMinutes = Minutes * 60;
                    Log.d(TAG, "Checking units, milihours = " + miliHours + ", miliMinutes = " + miliMinutes);

                    //need if statements to check is amy textviews are null, as of now the app crashes

                    secondIntegerTimeSet = Seconds + miliMinutes + miliHours;


//                    if (miliHours != 0 & miliMinutes != 0 & Seconds != 0) {
//                        secondIntegerTimeSet = Seconds + miliMinutes + miliHours;
//                    } else if (miliHours == 0) {//
//                        secondIntegerTimeSet = Seconds + miliMinutes;
//                    } else if (miliMinutes == 0) {
//                        secondIntegerTimeSet = Seconds + miliHours;
//                    } else if (Seconds == 0) {
//                        secondIntegerTimeSet = miliHours + miliMinutes;
//                    } else if (miliHours == 0 & miliMinutes == 0) {
//                        secondIntegerTimeSet = Seconds;
//                    } else if (miliHours == 0 & Seconds == 0) {
//                        secondIntegerTimeSet = miliMinutes;
//                    } else if (miliMinutes == 0 & Seconds == 0) {
//                        secondIntegerTimeSet = miliHours;
//                    } else if (miliHours == 0 & miliMinutes == 0 & Seconds == 0) {
//                        secondIntegerTimeSet = 0;
                        Log.d(TAG, "Total seconds  = " + secondIntegerTimeSet);
//                    }


                    //Integer secondIntegerTimeSet = Seconds + miliMinutes + miliHours;
                    Log.d(TAG, "Total seconds  = " + secondIntegerTimeSet);
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

                    extras.putInt("secondTimeValue", secondIntegerTimeSet);
                    //extras.putInt("minuteTimeValue",minuteIntegerTimeSet);
                    //extras.putInt("hourTimeValue",hourIntegerTimeSet);
                    intent.putExtras(extras);


                    startService(intent);
                    //startActivity(intent);
                }else Toast.makeText(getApplicationContext(), "No time indicated!", Toast.LENGTH_SHORT).show();

            }
        });





        saveAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp_address = EditaddressText.getText().toString();
                if(temp_address.length() > 0)
                {
                    sharePreferenceHelper.saveCheckInAddress(temp_address);
                }

            }
        });

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAmSafe = true;
                Log.d(TAG, "User has hit the check in button" );
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TimerRunning = true)
                {
                    resetValue = true;
                    hourEditText.setText(null);
                    minuteEditText.setText(null);
                    secondEditText.setText(null);

                    Intent intent = new Intent(checkInActivity.this, checkInActivity.class);
                    startActivity(intent);
                }
                else Toast.makeText(getApplicationContext(), "No time indicated!", Toast.LENGTH_SHORT).show();


            }
        });

    }

/*
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

 */

public void finalTimer()
{
    int secondIntegerTimeSet = 10;
    Intent newintent = new Intent(checkInActivity.this, CheckinService.class);
    Bundle extras = new Bundle();

    extras.putInt("secondTimeValue",secondIntegerTimeSet);
    newintent.putExtras(extras);

    startService(newintent);
}




}
