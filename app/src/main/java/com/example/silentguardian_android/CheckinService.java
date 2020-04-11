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
import static android.content.Intent.getIntentOld;
import static android.content.Intent.makeMainActivity;
import static java.util.ResourceBundle.getBundle;

public class CheckinService extends Service {

    //Integer Hours;
   // Integer Minutes;
    //Integer Seconds;
    Boolean firstTimerDone= false;



    private static final String TAG = "CheckIn";
    private static final String TAG2 = "CheckIn2";
    private static final String CHANNEL_ID = "NotificationChannelID";
    //public static final int VISIBILITY_PUBLIC = 1;

    protected SharePreferenceHelper sharePreferenceHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        sharePreferenceHelper = new SharePreferenceHelper(this);
        Log.d(TAG2, "create");
        sharePreferenceHelper.firstTimerDoneService(false);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        sharePreferenceHelper = new SharePreferenceHelper(this);

        Log.d(TAG2, "start");
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

//trying to use shared preferences to stop timer ONCE AND FOR ALL


            //this is the Seconds Timer
            final Timer secondstimer = new Timer();
            secondstimer.scheduleAtFixedRate(new TimerTask() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {

                    Log.d(TAG, "Reached inside timer function ");

                    Intent localintent = new Intent();
                    localintent.setAction("Counter");

                    //trying to use shared preferences to stop timer ONCE AND FOR ALL
                    if(sharePreferenceHelper.getresetTimerValue()==false)
                    {

                        secondTimeRemaining[0]--;

                        NotificationUpdate(secondTimeRemaining[0]);

                        if (secondTimeRemaining[0] <= 0) {
                            secondstimer.cancel();
                        }
                        localintent.putExtra("TimeRemaining", secondTimeRemaining[0]);
                        sendBroadcast(localintent);
                    }



                    else if (sharePreferenceHelper.getresetTimerValue()==true)
                    {

                        Log.d(TAG, "Checking to see if i entered the reset timer loop in the service class ");
                        secondstimer.cancel();
                        secondTimeRemaining[0]=0;
                        localintent.putExtra("TimeRemaining", secondTimeRemaining[0]);
                        sendBroadcast(localintent);
                        stopSelf();
                    }


                }
                // dont want any delay, the period is 1000ms, means 1 second
            }, 0, 1000);






        return super.onStartCommand(intent, flags, startId);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void NotificationUpdate(Integer timeLeft)
    {
        Log.d(TAG2, "notificationupdate");
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




            //this notifcation is showing during the time between first timer going off and before last timer goes off
            if (sharePreferenceHelper.getfirstTimerDoneService()) {
                Log.d(TAG2, "first if");
                final Notification[] notification = {new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Please Check-in with Silent Guardians")
                        .setContentText("Time Remaining : " + notificationHours + ":" + notificationMinutes + ":" + notificationSeconds)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build()};

                startForeground(1, notification[0]);

                /*

                NotificationChannel notificationChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d(TAG2, "second if");
                    notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_LOW);
                    notificationChannel.setSound(null, null);
                    notificationChannel.enableVibration(false);
                }
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d(TAG2, "third");
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                 */




            }

            //This is the last notification: tells the users the messages have been sent
            if (Hours ==0 & Minutes ==0 & Seconds ==0 & sharePreferenceHelper.getfirstTimerDoneService()) {
                Log.d(TAG2, "fourth if");
                final Notification[] notification = {new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Missed Check missed: Messages sent to Guardians")
                        .setContentText("Time Remaining : " + notificationHours + ":" + notificationMinutes + ":" + notificationSeconds)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()};

                startForeground(1, notification[0]);

                /*
                NotificationChannel notificationChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_DEFAULT);
                    Log.d(TAG2, "fifth if");

                }
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(notificationChannel);
                    Log.d(TAG2, "sixth if");
                }

                 */

                //resetting boolean so it can show correct notification if running again
                //firstTimerDone= false;



            }



            //this notification tells the user to check in because their first timer has gone off
            if (Hours ==0 & Minutes ==0 & Seconds ==0 & !sharePreferenceHelper.getfirstTimerDoneService())
            {

                Log.d(TAG2, "seventh if");
                sharePreferenceHelper.firstTimerDoneService(true);
                //firstTimerDone = true;

                messageGPSHelper.vibrate();

                final Notification[] notification = {new NotificationCompat.Builder(this , CHANNEL_ID)
                        .setContentTitle("Please Check-in with Silent Guardians")
                        .setContentText("Time Remaining : " + notificationHours + ":" + notificationMinutes + ":" + notificationSeconds)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build()};

                        startForeground(1,notification[0]);

                        /*
                NotificationChannel notificationChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_DEFAULT);
                    Log.d(TAG2, "eighth if");

                }
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d(TAG2, "nineth if");
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                         */


            }







            //this is when the notification just shows the regular countdown
             if((Hours>0 || Minutes>0 || Seconds>0) & !sharePreferenceHelper.getfirstTimerDoneService()){
                 Log.d(TAG2, "tenth if");


                 NotificationChannel notificationChannel = null;
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_LOW);
                     notificationChannel.setSound(null, null);
                     notificationChannel.enableVibration(false);
                     Log.d(TAG2, "11 if");
                 }
                 NotificationManager notificationManager = getSystemService(NotificationManager.class);
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                     Log.d(TAG2, "12 if");
                     notificationManager.createNotificationChannel(notificationChannel);
                 }

/*
                 NotificationChannel notificationChannel = null;
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_DEFAULT);
                     notificationChannel.setSound(null, null);
                     notificationChannel.enableVibration(false);


                 }
                 NotificationManager notificationManager = getSystemService(NotificationManager.class);
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     notificationManager.createNotificationChannel(notificationChannel);
                 }

 */




                 Log.d(TAG, "Reached remaining time section");
                //regular notification to show the user how much time is left on the timer

                final Notification[] notification = {new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("My Check-in Timer")
                        .setContentText("Time Remaining : " + notificationHours + ":" + notificationMinutes + ":" + notificationSeconds)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .build()};

                startForeground(1, notification[0]);

                //the following if statements make sure the proper android phones are up to date or will crash
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library


/*
                 NotificationChannel notificationChannel = null;
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_LOW);
                     //notificationChannel.setSound(null, null);
                     //notificationChannel.enableVibration(false);
                 }
                 NotificationManager notificationManager = getSystemService(NotificationManager.class);
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     assert notificationManager != null;
                     notificationManager.createNotificationChannel(notificationChannel);
                 }
*/


             }






                 //startForeground(1, notification[0]);

            /*
                NotificationChannel notificationChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_DEFAULT);


                }
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }

             */






        }

        catch (Exception e){
            e.printStackTrace();
        }


    }

    /*
 if( Hours==0 & Minutes==0 & Seconds==0 & userTimerDone==false)
    {
        //remember to make in onDestroy to clear this
        userTimerDone = true;

        Log.d(TAG, "User has 5 mins to hit i am safe button " );

        finalTimer();

    }

     */







}
