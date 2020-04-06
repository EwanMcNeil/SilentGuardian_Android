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
        Context newContext = context.getApplicationContext();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(newContext)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(newContext, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(newContext, 0, notificationIntent, 0);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


    }
}
