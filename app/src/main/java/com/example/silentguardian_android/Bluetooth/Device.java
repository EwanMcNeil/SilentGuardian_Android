package com.example.silentguardian_android.Bluetooth;

import android.bluetooth.BluetoothDevice;

public class Device {
    private BluetoothDevice mDevice;
    private int rssi;

    public Device(BluetoothDevice mDevice){
        this.mDevice = mDevice;
    }
    public String getAddress() {
        return mDevice.getAddress();
    }

    public String getName() {
        return mDevice.getName();
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public int getRSSI() {
        return rssi;
    }
}
