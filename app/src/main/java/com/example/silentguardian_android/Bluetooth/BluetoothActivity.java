package com.example.silentguardian_android.Bluetooth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback {

    private final static String TAG = "__blueActivity";
    protected BluetoothManager mBluetoothManager;
    protected BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mConnectedGatt;

    protected Scanner_BTLE mBTLeScanner;



    protected static final int REQUEST_ENABLE_BT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SetupBluetooth();//helper function



    }

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void SetupBluetooth(){

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        //Check if bluetooth is enabled
        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else  Toast.makeText(this, "Bluetooth is enabled!", Toast.LENGTH_LONG).show();
    }
    private Runnable mStartRunnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            startScan();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT)
            Toast.makeText(this, "Thanks!", Toast.LENGTH_LONG).show();
    }

    private void startScan() {
        mBluetoothAdapter.startLeScan(this);
        setProgressBarIndeterminateVisibility(true);

        //mHandler.postDelayed(mStopRunnable, 2500);
    }
    public void stopScan() {

        mBTLeScanner.stop();
    }
    public void addDevice(BluetoothDevice device, int rssi) {

        //TODO
//        String address = device.getAddress();
//        if (!mBTDevicesHashMap.containsKey(address)) {
//            BTLE_Device btleDevice = new BTLE_Device(device);
//            btleDevice.setRSSI(rssi);
//
//            mBTDevicesHashMap.put(address, btleDevice);
//            mBTDevicesArrayList.add(btleDevice);
//        }
//        else {
//            mBTDevicesHashMap.get(address).setRSSI(rssi);
//        }
//
//        adapter.notifyDataSetChanged();
    }
}
