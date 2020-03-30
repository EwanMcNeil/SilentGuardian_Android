package com.example.silentguardian_android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.Notification.*;
import static android.content.Intent.getIntent;
import static android.content.Intent.makeMainActivity;
import static java.util.ResourceBundle.getBundle;

public class CheckinService extends Service {

    private static final String TAG = "CheckIn";

    private static final String CHANNEL_ID = "NotificationChannelID";
    //public static final int VISIBILITY_PUBLIC = 1;

    protected SharePreferenceHelper sharePreferenceHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sharePreferenceHelper = new SharePreferenceHelper(this);

        //this is for when there was only seconds, trying to replace this to recieve all three in a bundle
        //final Integer[] secondTimeRemaining = {intent.getIntExtra("secondTimeValue", 0)};

        Bundle bundle = intent.getExtras();



        //trying something out
        //had success in sending the dta through the method with just one value (seconds time)
        //will try to send the others now



        final Integer[] secondTimeRemaining = {bundle.getInt("secondTimeValue")};
        Log.d(TAG, "Checking if i get here = ");

/*
        if (bundle.getInt("secondTimeValue") ==0)
        {

            Log.d(TAG, "Checking if i get here = ");
            Intent newintent = new Intent(CheckinService.this, MainActivity.class);
            startActivity(newintent);
        }

 */



        Log.d(TAG, "Checking total seconds = " +bundle.getInt("secondTimeValue"));
        //final Integer[] minuteTimeRemaining = {bundle.getInt("minuteTimeValue")};
        //final Integer[] hourTimeRemaining = {bundle.getInt("hourTimeValue")};

        //trying to recieve the three values in the sent bundle from check in class
        /*
        final Integer[] secondTimeRemaining = {intent.getIntExtra("secondTimeValue", 0)};
        final Integer[] minuteTimeRemaining = {intent.getIntExtra("minuteTimeValue", 0)};
        final Integer[] hourTimeRemaining = {intent.getIntExtra("hourTimeValue", 0)};

         */
        Integer Hours = bundle.getInt("secondTimeValue") / 3600;
        Integer Minutes = ((bundle.getInt("secondTimeValue")) - (Hours*3600)) / 60;
        Integer Seconds = ((bundle.getInt("secondTimeValue")) - (Hours*3600)) - (Minutes*60);
        Log.d(TAG, "Checking Hours = " +Hours + " checking minutes = " +Minutes + " checking Seconds = " +Seconds);




        //this is the Seconds Timer
        final Timer secondstimer = new Timer();
        secondstimer.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {

                Intent localintent = new Intent();
                localintent.setAction("Counter");

                secondTimeRemaining[0]--;

                NotificationUpdate(secondTimeRemaining[0]);

                if(secondTimeRemaining[0] <=0)
                {
                    secondstimer.cancel();

                    //this is where you will start another clock ex. 5-10 mins, and if that timer hits 0...
                    //then thats when the app will send the address they saved (within the checkin activity)
                    // to their guardians
                }
                localintent.putExtra("TimeRemaining", secondTimeRemaining[0]);
                //localintent.putExtra("minutesTimeRemaining", minuteTimeRemaining[0]);
                //localintent.putExtra("hoursTimeRemaining", hourTimeRemaining[0]);
                sendBroadcast(localintent);
            }
            // dont want any delay, the period is 1000ms, means 1 second
        }, 0, 1000);



        return super.onStartCommand(intent, flags, startId);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void NotificationUpdate(Integer timeLeft)
    {

        Integer Hours = ((timeLeft)/3600);
        Integer Minutes = (timeLeft - (Hours*3600))/60;
        Integer Seconds = (timeLeft - (Hours*3600))- (Minutes *60);

        String notificationHours = Integer.toString(Hours);
        String notificationMinutes = Integer.toString(Minutes);
        String notificationSeconds = Integer.toString(Seconds);





        try
        {
            Intent notificationIntent = new Intent(this , checkInActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final Notification[] notification = {new NotificationCompat.Builder(this , CHANNEL_ID)
                    .setContentTitle("My Check-in Timer")
                    .setContentText("Time Remaining : " + notificationHours + ":" + notificationMinutes + ":" + notificationSeconds)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()};


            startForeground(1,notification[0]);
            //the following if statements make sure the proper android phones are up to date or will crash
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library

            NotificationChannel notificationChannel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_DEFAULT);
            }
            NotificationManager notificationManager = getSystemService (NotificationManager.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel (notificationChannel);
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }


    }

    /*
    public void fullScreenNotification()
    {
        Intent fullScreenIntent = new Intent(this, checkInActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setFullScreenIntent(fullScreenPendingIntent, true);



    }

     */



}
