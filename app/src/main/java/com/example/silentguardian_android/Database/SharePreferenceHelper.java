package com.example.silentguardian_android.Database;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.silentguardian_android.R;

public class SharePreferenceHelper extends AppCompatActivity {
    private static final String TAG = "sharedPrefHelper";
    protected SharedPreferences sharedPreferences;

    public SharePreferenceHelper(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void saveProfile(String name, String password)
    {



        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putString("password",password);

        editor.commit();


            Log.d(TAG, "it worked");



    }

    //at somepoint use this function
    /*public Person getProfile();
    {
        String name = sharedPreferences.getString("name", null);
        String password = sharedPreferences.getString("password",null);


        if(name != null){
            Log.d(TAG, "helper getProfile()");
            Log.d(TAG, name);
            Log.d(TAG,password);
        }
        else {
            //doesnt like toast,
            Log.d(TAG, "getProfile() name is null!!");
        }

        return new Person(name,password);
    }

    */

//user name getter
    public String userNameReturn(){

        //SharedPreferences.Editor editor = sharedPreferences.edit();
        String name = sharedPreferences.getString("name", null);

        return name;
    }

    //password getter
    public String passwordReturn(){

        String password = sharedPreferences.getString("password", null);
        if(password==null)
            return"it s null";
        return password;
    }



}