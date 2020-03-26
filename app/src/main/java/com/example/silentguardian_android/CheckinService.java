package com.example.silentguardian_android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.Notification.*;

public class CheckinService extends Service {

    private static final String CHANNEL_ID = "NotificationChannelID";
    public static final int VISIBILITY_PUBLIC = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        final Integer[] timeRemaining = {intent.getIntExtra("TimeValue", 0)};

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {

                Intent localintent = new Intent();
                localintent.setAction("Counter");

                timeRemaining[0]--;

                NotificationUpdate(timeRemaining[0]);

                if(timeRemaining[0] <=0)
                {

                    timer.cancel();

                    fullScreenNotification();

                    //this is where you will start another clock ex. 5-10 mins, and if that timer hits 0...
                    //then thats when the app will send the address they saved (within the checkin activity)
                    // to their guardians
                }
                localintent.putExtra("TimeRemaining", timeRemaining[0]);
                sendBroadcast(localintent);

            }
            // dont want any delay, the period is 1000, means 1 second
        }, 0, 1000);


        return super.onStartCommand(intent, flags, startId);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void NotificationUpdate(Integer timeLeft)
    {

        try
        {
            Intent notificationIntent = new Intent(this , checkInActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final Notification[] notification = {new NotificationCompat.Builder(this , CHANNEL_ID)
                    .setContentTitle("My Check-in Timer")
                    .setContentText("Time Remaining : " + timeLeft.toString())
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



}
