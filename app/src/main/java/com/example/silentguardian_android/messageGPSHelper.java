package com.example.silentguardian_android;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import java.util.List;



///MessagerGPSHElper has the functions to get GPS coordinates and to send a SMS message with a configureable message


public class messageGPSHelper {
    private static double TODO = 1;

    Context mContext;
    public String bestProvider;
    public Criteria criteria;

    public messageGPSHelper(Context in_context) {
        this.mContext = in_context;
    }


    //fucntion to return the logitude of the current location
    protected double getLong() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return TODO;
        }



        Location location = getLastKnownLocation();
        if(location == null){
            return 88.0;
        }

        double longitude = location.getLongitude();

        return longitude;
    }


    //function to return the latitude of the current location
    protected double getLat() {

        LocationManager lm = (LocationManager)  mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return TODO;
        }

        Location location = getLastKnownLocation();
        if(location == null){
            return 88.0;
        }

        double latitude = location.getLatitude();
        return latitude;
    }


    // methods getlat() and getLong() call this function in order to find the previous location
    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions

            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }




    //takes in a phone number and a string to send a message
    //possibly within here call the local methods getlat and getlong
    public void sendMessage(String number, String message){
        try{

            String messageOut = message + "My location is: " + messageLocationLink();
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(number,null,messageOut, null,null);
            Toast.makeText(mContext, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(mContext, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }

    }


    //local location link to create the link for google maps
    protected String messageLocationLink(){

        double  latitude =  getLat();
        double longitude = getLong();
        String output = "https://www.google.com/maps/search/?api=1&query=" +latitude + "," +longitude;

        return output;
    }




}

