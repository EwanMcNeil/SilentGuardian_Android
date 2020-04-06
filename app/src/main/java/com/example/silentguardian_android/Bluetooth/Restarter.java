package com.example.silentguardian_android.Bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


public class Restarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
       

    }
}
