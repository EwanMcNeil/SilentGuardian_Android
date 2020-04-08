package com.example.silentguardian_android.Bluetooth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.silentguardian_android.MainActivity;
import com.example.silentguardian_android.R;


public class Restarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
//        Intent notificationIntent = new Intent(context , MainActivity.class);
//        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//
//        final Notification[] notification = {new NotificationCompat.Builder(context, "CHANNEL_ID_BLUETOOTH")
//                .setContentTitle("SIlent guard")
//                .setContentText("Please reconect device ")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .build()};
//
//
//
//        // Add as notification
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0,);

        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // notification icon
                .setContentTitle("Notification!") // title for notification
                .setContentText("Hello word") // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intentNotify = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,intentNotify,0);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());


    }
}
