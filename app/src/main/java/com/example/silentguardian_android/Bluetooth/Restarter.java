package com.example.silentguardian_android.Bluetooth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.silentguardian_android.MainActivity;
import com.example.silentguardian_android.R;


public class Restarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");

        Intent newintent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newintent, 0);


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "restarterNotify")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("SilentGuardian")
                    .setContentText("SilentGuardian Device has been disconnected")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("SilentGuardian Device has been disconnected"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            NotificationChannel channel = new NotificationChannel("restarterNotify", "restarted", NotificationManager.IMPORTANCE_DEFAULT);
            //channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);



            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(15, builder.build());
        }
        else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher_foreground) // notification icon
                    .setContentTitle("Notification!") // title for notification
                    .setContentText("Hello word") // message for notification
                    .setAutoCancel(true); // clear notification after click
            Intent intentNotify = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intentNotify, 0);
            mBuilder.setContentIntent(pi);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
        }

    }
}
