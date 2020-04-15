package com.example.silentguardian_android.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;


import android.app.ActivityManager;
import android.content.Context;

import android.app.Dialog;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silentguardian_android.Bluetooth.DeviceService;
import com.example.silentguardian_android.Fragments.iamsafeFragment;
import com.example.silentguardian_android.Helpers.AudioDatabase;
import com.example.silentguardian_android.Helpers.DatabaseHelper;
import com.example.silentguardian_android.Helpers.Person;
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;

import com.example.silentguardian_android.Helpers.audioFile;
import com.example.silentguardian_android.Helpers.messageGPSHelper;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.Tutorial.MyImage;
import com.example.silentguardian_android.Tutorial.TutorialViewpagerAdapter;
import com.example.silentguardian_android.Fragments.InsertPasswordCheckFragment;
import com.example.silentguardian_android.helpLinks.ResourceActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class mainActivity extends AppCompatActivity {


    private static SharePreferenceHelper sharePreferenceHelper;
    protected static ImageButton allclearImageButton;
   // protected SharePreferenceHelper sharePreferenceHelper;
    protected static ImageButton buttonTutorial;
    MediaRecorder recorder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharePreferenceHelper = new SharePreferenceHelper(this);
        //initializing the sentmessage
        if (sharePreferenceHelper.getMessageSent() == 2) {
            sharePreferenceHelper.setMessageSent(0);
        }


        allclearImageButton = findViewById(R.id.safeimageButton);


        buttonTutorial = findViewById(R.id.buttonTutorialMain);



        //permission check
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.VIBRATE,
                android.Manifest.permission.FOREGROUND_SERVICE
        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        final Dialog mInfoDialog = new Dialog(mainActivity.this, R.style.Theme_AppCompat);
        loadActivityTutorial(mInfoDialog);


        //if the user doesn't have an account existing, this if statement takes them to profile activity to create first profile
        if (sharePreferenceHelper.userNameReturn() == null) {
            Intent intent = new Intent(mainActivity.this, profileActivity.class);
            startActivity(intent);

        }


        allclearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int recent = sharePreferenceHelper.getMessageSent();
                if (recent == 1) {
//                    Intent intent = new Intent(mainActivity.this, allclearActivity.class);
//                    startActivity(intent);
                   iamsafeFragment dialog = new  iamsafeFragment();
                    dialog.show(getSupportFragmentManager(), "safe frag");
                } else {
                    startRecording();
                    alertPressed();
//                    sendMessageFragment dialog = new sendMessageFragment();
//                    dialog.show(getSupportFragmentManager(), "sendmessage_fragment");
                }
            }
        });


    }

    public void startRecording() {
        final SharePreferenceHelper helper = new SharePreferenceHelper(this);
        if (helper.checkifrecording() == false && helper.audioCheck() == true) {
            helper.recordingStart();
            AudioDatabase adb = new AudioDatabase(this);
            String fileName = getExternalCacheDir().getAbsolutePath();
            int num = adb.numberAudioObjects();
            num = num + 1;
            String newfilename = fileName + "/audiorecordtest" + num + ".3gp";
            Date currentTime = Calendar.getInstance().getTime();
            String date = currentTime.toString();
            audioFile file = new audioFile(date, newfilename);
            adb.insertFile(file);

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(newfilename);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                recorder.prepare();
            } catch (IOException e) {
                Log.e("hiya", "prepare() failed");
            }

            recorder.start();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            Log.i("tag", "This'll run 5000 milliseconds later");

                            recorder.stop();
                            recorder.release();
                            recorder = null;
                            helper.recordingStop();

                        }
                    },
                    5000);
        }
    }


    private void alertPressed() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SharePreferenceHelper spHelper = new SharePreferenceHelper(this);

        List<Person> thresholdOnePeople;
        thresholdOnePeople = dbHelper.getThresholdOne();
        String[] thresholdOneNumbers = new String[thresholdOnePeople.size()];

        for (int i = 0; i < thresholdOnePeople.size(); i++) {
            thresholdOneNumbers[i] = thresholdOnePeople.get(i).getPhoneNumber();
        }

        int Size = thresholdOnePeople.size();
        messageGPSHelper textHelper = new messageGPSHelper(this);

        for (int i = 0; i < Size; i++) {
            textHelper.sendMessage(thresholdOneNumbers[i], spHelper.ThresholdOneMessageReturn());
        }

        updateAllClearButton();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateAllClearButton();

        if (!(isMyServiceRunning(DeviceService.class))) {
            Toast.makeText(this, R.string.connectDevice, Toast.LENGTH_LONG).show();
        }

    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    public static void updateAllClearButton() {
        int output = sharePreferenceHelper.getMessageSent();


        if (output == 0) {


            allclearImageButton.setBackgroundResource(R.drawable.redbutton_3);


            //needs to be changed to somthing else
        } else {


            allclearImageButton.setBackgroundResource(R.drawable.greenbutton_3);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharePreferenceHelper.logOut();//trying to find a way to reset the log in this way
    }

    ///code for the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        InsertPasswordCheckFragment dialog = new InsertPasswordCheckFragment();
        Bundle args = new Bundle();
        Intent intent;

        switch (item.getItemId()) {
            case R.id.bluetoothSettingsdropdown:

                args.putString("intent", "bluetooth");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "password");
                return true;
            case R.id.profileSettingdropdown:
                args.putString("intent", "profile");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "password");
                return true;

            case R.id.sendTestMessageDropDown:
                Intent intentnew = new Intent(mainActivity.this, ResourceActivity.class);
                startActivity(intentnew);
                return true;
            case R.id.switchLanguage:
                switchLanguage();
                return true;
            case R.id.thresholdsettingdropdown:
                //checking if password matches from user to sharedpreferences
                args.putString("intent", "threshold");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "password");
                return true;

            case R.id.checkIndropdown:
                intent = new Intent(mainActivity.this, checkinActivity.class);
                startActivity(intent);
                return true;

            case R.id.recordingsActivity:
                intent = new Intent(mainActivity.this, playbackActivity.class);
                startActivity(intent);
                return true;
            case R.id.enableaudio:
                args.putString("intent", "enableAudio");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "password");

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void switchLanguage() {
        String current = Locale.getDefault().getLanguage();
        if (current.equals("fr")) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            sharePreferenceHelper.saveLanguage("en");
            recreate();
        } else if (current.equals("en")) {
            Locale locale = new Locale("fr");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            sharePreferenceHelper.saveLanguage("fr");
            recreate();
        }
    }

    private void loadActivityTutorial(final Dialog mInfoDialog) {

        final List<MyImage> mList = new ArrayList<>();
        mList.add(new MyImage("Send an alert",
                "Alert your Guardians, Level 1 or Level 2, by either pressing the the Silent Guardians device or the I am in danger button on the main page of the App. "
                , R.mipmap.danger_button_tuto));


        mList.add(new MyImage("Features",
                "Access your audio recordings and Check-In Guardian."
                , R.mipmap.click_feature));

        mList.add(new MyImage("Settings & Miscellaneous",
                "Press the Setting Icon to modify your profile:\n"
                        + "Add/remove contact to the App\n"
                        + "Add/remove Guardians to/from your Guardian Levels\n"
                        + "Connect your Silent Guardian device"
                , R.mipmap.main_menu_tut
                , false));


        buttonTutorial.setOnClickListener(new View.OnClickListener() {
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


}








