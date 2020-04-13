package com.example.silentguardian_android.Bluetooth;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.silentguardian_android.AudioRecordTest;
import com.example.silentguardian_android.Database.AudioDatabase;
import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.Database.SharePreferenceHelper;
import com.example.silentguardian_android.Database.audioFile;
import com.example.silentguardian_android.MainActivity;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.checkInActivity;
import com.example.silentguardian_android.messageGPSHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DeviceService extends Service {


    ///objects from DeviceControlActivity
    private final static String TAG ="DeviceService";
    private final static int THREAD_DELAY = 1000;
    private static final int  THREAD_PERIOD = 10;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private int[] RGBFrame = {0,0,0};

    private String mDeviceName;
    private String mDeviceAddress;

    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;
    private String[] thresholdOneNumbers;
    private String[] thresholdTwoNumbers;


    private Timer timerNew = null;

    public final static UUID HM_RX_TX =
            UUID.fromString(SampleGattAttributes.HM_RX_TX);

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private boolean sendOne = false;

    //end objects from deviceControlActivity

    public int counter=0;
    private AudioDatabase adb;
    private static String fileName = null;
    private MediaRecorder recorder = null;



    // Code to manage Service lifecycle.(NEW)
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                //finish(); //finish isnt defined here
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };



    //NEW
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.e(TAG, "mGattUpdate Connection ");
                mConnected = true;
                //updateConnectionState(R.string.connected);

            }else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                unregisterReceiver( mGattUpdateReceiver);
                noDevice();
            }else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.e(TAG, "GATTSERVICESlaunch");
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            }
        }
    };


    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            Log.e(TAG, "GATTSERVICES NULL");
            return;
        }
        Log.e(TAG, "GATTSERVICES RAN");
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();


        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));

            // If the service exists for HM 10 Serial, say so.
          //  if(SampleGattAttributes.lookup(uuid, unknownServiceString) == "HM 10 Serial") { isSerial.setText("Yes, serial :-)"); } else {  isSerial.setText("No, serial :-("); }
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // get characteristic when UUID matches RX/TX UUID
            characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
            characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
        }

    }


    @Override
    public void onCreate() {
        //NEW
        Log.e(TAG, "onCreate");
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        //for audio
        adb = new AudioDatabase(this);
        fileName = getExternalCacheDir().getAbsolutePath();
        //This pulls in all the numbers for the people in the database
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        List<Person> thresholdOnePeople;
        thresholdOnePeople = dbHelper.getThresholdOne();
        thresholdOneNumbers = new String[thresholdOnePeople.size()];

        for(int i = 0; i < thresholdOnePeople.size(); i++){
            thresholdOneNumbers[i] = thresholdOnePeople.get(i).getPhoneNumber();
        }

        List<Person> thresholdTwoPeople;
        thresholdTwoPeople = dbHelper.getThresholdTwo();
        thresholdTwoNumbers = new String[thresholdTwoPeople.size()];
        for(int i = 0; i < thresholdTwoPeople.size(); i++){
            thresholdTwoNumbers[i] = thresholdTwoPeople.get(i).getPhoneNumber();
        }

        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else{
           // startForeground(1, new Notification());
            Intent notificationIntent = new Intent(this , MainActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final Notification[] notification = {new NotificationCompat.Builder(this, "CHANNEL_ID_BLUETOOTH")
                    .setContentTitle(getResources().getString(R.string.bluetooth_notif_title))
                    .setContentText(getResources().getString(R.string.bluetooth_notif_text))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build()};

            startForeground(1, notification[0]);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        Log.e(TAG, "startMyOwnForeground");
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand");
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }

        startTimer();
        return START_STICKY;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
        unbindService(mServiceConnection);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        broadcastIntent.putExtra("name", mDeviceName);
        broadcastIntent.putExtra("address",mDeviceAddress);
        this.sendBroadcast(broadcastIntent);


    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        Log.e(TAG, "timer");
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, String.valueOf(mConnected));
                if(characteristicTX == null) {
                    Log.d(TAG, "TX NULL");
                }
                if(mConnected && characteristicTX != null)
                    makeChange();

            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // on change of bars write char
    //this respsonds to changes

    private void makeChange() {
        String str = RGBFrame[0] + "," + RGBFrame[1] + "," + RGBFrame[2] + "\n";
        Log.d(TAG, "Sending result=" + str);
        final byte[] tx = str.getBytes();

        Log.d(TAG, String.valueOf(mConnected));

        if (mConnected) {
            mBluetoothLeService.readCharacteristic(characteristicTX);
            byte[] values = characteristicTX.getValue();
            if (values != null) {
                final int value = characteristicTX.getValue()[0];
                Log.d(TAG, "Value: " + Integer.toString(value));
                Log.d(TAG, "Value=" + value);
                //code for sending one text message
                if((value == 1 || value == 2) && !sendOne){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(!sendOne) {
                                messageGPSHelper textHelper = new messageGPSHelper(getApplicationContext());
                                SharePreferenceHelper spHelper = new SharePreferenceHelper(getApplicationContext());
                                if(value==1) {
                                    for(int i = 0; i < thresholdOneNumbers.length; i++) {
                                        textHelper.sendMessage(thresholdOneNumbers[i],spHelper.ThresholdOneMessageReturn());
                                    }
                                    sendOne = true;

                                        startRecording();

                                }
                                if(value==2) {
                                    for(int i = 0; i < thresholdTwoNumbers.length; i++) {
                                        Log.d("messages",thresholdTwoNumbers[i]);
                                        textHelper.sendMessage(thresholdTwoNumbers[i],spHelper.ThresholdTwoMessageReturn());
                                        //startRecording();
                                        //needs to be called here
                                    }
                                    sendOne = true;
                                        startRecording();

                                }
                           Log.i("Count", "=========  "+ (counter++));
                            }
                        }
                    });
                }
                sendOne = false;
            }
        }
    }



    public void startRecording() {
        final SharePreferenceHelper helper = new SharePreferenceHelper(this);
        if(helper.checkifrecording() == false && helper.audioCheck() ==true) {
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




   private void noDevice(){
       this.stopSelf();
   }



}
