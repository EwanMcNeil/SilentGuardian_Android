package com.example.silentguardian_android.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silentguardian_android.Helpers.CheckinService;
import com.example.silentguardian_android.Helpers.DatabaseHelper;
import com.example.silentguardian_android.Helpers.Person;
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.Helpers.messageGPSHelper;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.Tutorial.MyImage;
import com.example.silentguardian_android.Tutorial.TutorialViewpagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.*;

public class checkinActivity extends AppCompatActivity {

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
    protected Boolean userTimerDone = false;
    protected Boolean iAmSafe = false;
    protected Boolean resetValue = false;
    protected BroadcastReceiver broadcastReceiver;
    protected messageGPSHelper SMSHelper;

    protected ImageButton mButtonTutorial;

    private static final String TAG = "CheckIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
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

        welcomeText = findViewById(R.id.welcomeTextView);
        explainAddressText = findViewById(R.id.addressExplainTextView);
        EditaddressText = findViewById(R.id.EditaddressText);
        setTimerText = findViewById(R.id.setTimerTextView);
        saveAddressButton = findViewById(R.id.saveAddressButton);
        startTimerButton = findViewById(R.id.startTimerButton);
        checkInButton = findViewById(R.id.checkInButton);
        restartButton = findViewById(R.id.restartButton);

        secondEditText = findViewById(R.id.secondsEditText);
        minuteEditText = findViewById(R.id.minutesEditText);
        hourEditText = findViewById(R.id.hourEditText);

        mButtonTutorial = findViewById(R.id.buttonTutorialCheckin);
        sharePreferenceHelper = new SharePreferenceHelper(this);

        SMSHelper = new messageGPSHelper(this);

        sharePreferenceHelper.resetTimerValue(false);
        sharePreferenceHelper.iAmSafe(false);


        final Dialog mInfoDialog = new Dialog(checkinActivity.this, R.style.Theme_AppCompat);
        loadActivityTutorial(mInfoDialog);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Counter");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                /*
                if(resetValue ==true)
                {
                    Log.d(TAG, "Reached the final reset loop");
                    hourEditText.setText(null);
                    minuteEditText.setText(null);
                    secondEditText.setText(null);
                    context.stopService(intent);
                    recreate();

                    TimerRunning = false;
                    userTimerDone=false;
                    iAmSafe=false;
                    resetValue = false;

                }

                 */


                Integer Hours = (intent.getIntExtra("TimeRemaining", 0)) / 3600;
                hourEditText.setText(Hours.toString() + "   :");
                Log.d(TAG, "Checking  final Hours = " + Hours);

                Integer Minutes = (((intent.getIntExtra("TimeRemaining", 0)) - (Hours * 3600)) / 60);
                minuteEditText.setText(Minutes.toString() + "   :");
                Log.d(TAG, "Checking  final Minutes = " + Minutes);

                Integer Seconds = ((intent.getIntExtra("TimeRemaining", 0)) - (Hours * 3600) - (Minutes * 60));
                secondEditText.setText(Seconds.toString());
                Log.d(TAG, "Checking  final Seconds = " + Seconds);


                if (Hours == 0 & Minutes == 0 & Seconds == 0 & userTimerDone == true & sharePreferenceHelper.getresetTimerValue() == false) {
                    Log.d(TAG, "Entered the last loop ");

                    if (!sharePreferenceHelper.getiAmSafe()) {
                        //creating db object to use the functions
                        DatabaseHelper dbhelper = new DatabaseHelper(getBaseContext());
                        List<Person> people = dbhelper.getThresholdOne();

                        // if (!sharePreferenceHelper.returnCheckInAddress().toString().matches(""))
                        //{


                        Log.d(TAG, "texting address loop ");
                        //}
                        ////create loop that will message the "I am Safe" message to all guardians who are in threshold one
                        for (int i = 0; i < people.size(); i++) {

                            String temp = " ";

                            temp = people.get(i).getPhoneNumber();

                            String message = "Please call or text me, I have missed my Check-in with Silent Guardians, I was heading to this location: " + sharePreferenceHelper.returnCheckInAddress() + ".";

                            //change this back to sendMessage after testing
                            SMSHelper.sendMessage(temp, message);

                            Log.d(TAG, "Missed Check-in messages " + i + " has been sent. ");
                            hourEditText.setText("");//TODO used to crash because there was no reset of the editTExt
                            minuteEditText.setText("");
                            secondEditText.setText("");

                            //need to reset these in order to start timer again correctly after messages has been sent
                            //TimerRunning = false;
                            //userTimerDone=false;
                            //iAmSafe=false;
                            //resetValue = false;


                            sharePreferenceHelper.firstTimerDoneService(false);
                            //recreate();
                            //this works better for when the message is sent, can use the check in again afterwards.
                            finish();


                        }
                    } else if (sharePreferenceHelper.getiAmSafe()) {

                        //sharePreferenceHelper.firstTimerDoneService(false);
                        Log.d(TAG, "testing to see if i enter the i am safe test ");
                        finish();
                        Intent newintent = new Intent(checkinActivity.this, mainActivity.class);
                        startActivity(newintent);

                        sharePreferenceHelper.firstTimerDoneService(false);
                    }

                    //not sure if i need an else here;


                }


                //this is where i will set a function to check if the user timer has gone off
                //if it has, i still start a new timer that will be 5-10 mins, if they fail to hit the i am safe within this clock, the message will
                //send out.
                if (Hours == 0 & Minutes == 0 & Seconds == 0 & userTimerDone == false & sharePreferenceHelper.getresetTimerValue() == false & !sharePreferenceHelper.getiAmSafe()) {
                    //remember to make in onDestroy to clear this
                    userTimerDone = true;

                    Log.d(TAG, "User has 5 mins to hit i am safe button ");


                    finalTimer();


                }


                //where to put last reset if statement
                if (Hours == 0 & Minutes == 0 & Seconds == 0 & sharePreferenceHelper.getresetTimerValue()) {
                    hourEditText.setText("");
                    minuteEditText.setText("");
                    secondEditText.setText("");

                    TimerRunning = false;
                    userTimerDone = false;
                    //iAmSafe=false;
                    //resetValue = false;

                    //sharePreferenceHelper.resetTimerValue(false);

                    //this is used to reset all of the booleans and ensures the activity can function after the reset timer has been pressed.
                    recreate();
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
                        || !secondEditText.getText().toString().matches("")) {//dont really need the main check, it s just send a toast user presses start timer without indicating a time

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


                    Intent intent = new Intent(checkinActivity.this, CheckinService.class);
                    Bundle extras = new Bundle();

                    //extras.putInt("TimerStartClicked",TimerStartClicked);

                    extras.putInt("secondTimeValue", secondIntegerTimeSet);
                    //extras.putInt("minuteTimeValue",minuteIntegerTimeSet);
                    //extras.putInt("hourTimeValue",hourIntegerTimeSet);
                    intent.putExtras(extras);


                    startService(intent);

                    Log.d(TAG, "checking to see if the code gets this far when timer is clicked for firstrun crash");
                    //startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "No time indicated!", Toast.LENGTH_SHORT).show();

            }
        });


        saveAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp_address = EditaddressText.getText().toString();
                if (temp_address.length() > 0) {
                    sharePreferenceHelper.saveCheckInAddress(temp_address);
                    Toast.makeText(getApplicationContext(), R.string.addressSaved, Toast.LENGTH_SHORT).show();
                }

            }
        });

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharePreferenceHelper.iAmSafe(true);
                Log.d(TAG, "User has hit the check in button");
                Toast.makeText(getApplicationContext(), R.string.checkInSaved, Toast.LENGTH_SHORT).show();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TimerRunning = true) {
                    //resetValue = true;
                    hourEditText.setText(null);
                    minuteEditText.setText(null);
                    secondEditText.setText(null);

                    sharePreferenceHelper.resetTimerValue(true);


                    //Intent newintent = new Intent(checkInActivity.this, CheckinService.class);
                    //getBaseContext().stopService(newintent);
                    Log.d(TAG, "User has hit the Restart Timer Button");
                    //recreate();
                    //Intent intent = new Intent(checkInActivity.this, checkInActivity.class);
                    //startActivity(intent);


                } else
                    Toast.makeText(getApplicationContext(), R.string.noTime, Toast.LENGTH_SHORT).show();


            }
        });

    }

    /*
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }

     */
    private void loadActivityTutorial(final Dialog mInfoDialog) {

        final List<MyImage> mList = new ArrayList<>();
        mList.add(new MyImage("Going somewhere? Let your Guardians know!",
                "Let your guardians know where you are going. If you do not Check-in with the App within a certain period of time, you rguardians will be alerted that you might be unsafe."
                , R.mipmap.silent_guardians_logo1));


        mList.add(new MyImage("Indicate your destination",
                "Enter the address you're planning to go to."
                , R.mipmap.check_n_address));

        mList.add(new MyImage("Indicate how long before checking-in",
                "Set the time you have to check-in before the notification is sent to your Guardians."
                , R.mipmap.check_n_time
                , false));

        mList.add(new MyImage("Timer notification",
                "Keep track of how much time there is left in your timer without having to go to the App."
                , R.mipmap.check_n_notif
                , false));

        mList.add(new MyImage("Check-in with Silent Guardian",
                "Once your timer expires, you will have a grace period of 2 minutes to press the I am Safe button. Otherwise an alert will be sent to your guardians."
                , R.mipmap.check_n_time
                , false));


        mButtonTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mInfoDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mInfoDialog.setContentView(R.layout.activity_tutorial);
                //UI elements
                ViewPager mScreenPager = mInfoDialog.findViewById(R.id.screen_viewpager);
                TabLayout mTabIndicator = mInfoDialog.findViewById(R.id.tab_indicator);
                TextView mSkip = mInfoDialog.findViewById(R.id.tv_skip);
                final Button mDialogButton = mInfoDialog.findViewById(R.id.btn_get_started);
                mSkip.setVisibility(View.INVISIBLE);
                //decodeSampledBitmapFromResource(getResources(),R.drawable.guardians_act_info, 220, 220);
                TutorialViewpagerAdapter mTutorialViewpagerAdapter = new TutorialViewpagerAdapter(getApplicationContext(), mList, false);
                mScreenPager.setAdapter(mTutorialViewpagerAdapter);
                // setup tablayout with viewpager
                mTabIndicator.setupWithViewPager(mScreenPager);
                mDialogButton.setText(R.string.end_tutorial_button_text);
                // if button is clicked, close the custom dialog
                mTabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == mList.size() - 1) {
                            mDialogButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override//must have these two here
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
                mDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mInfoDialog.dismiss();
                    }
                });
                mInfoDialog.show();
            }
        });
    }


    public void finalTimer() {

        if (sharePreferenceHelper.getresetTimerValue() == false) {

            int secondIntegerTimeSet = 10;
            Intent newintent = new Intent(checkinActivity.this, CheckinService.class);
            Bundle extras = new Bundle();

            extras.putInt("secondTimeValue", secondIntegerTimeSet);
            newintent.putExtras(extras);

            Log.d(TAG, "in final timer");

            startService(newintent);
        }


    }


    @Override
    protected void onStop() {
        //unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}
