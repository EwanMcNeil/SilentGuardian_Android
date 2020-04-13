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

        sharedPreferences = context.getSharedPreferences("silent_guardian",Context.MODE_PRIVATE);

    }

    public void saveProfile(String name, String password)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putString("password",password);

        editor.commit();


            Log.d(TAG, "it worked");



    }


    //function to save the first threshold message
    public void saveThresholdOneMessage( String message)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Threshold One Message", message);

        editor.commit();

    }

    //function to save the second threshold message
    public void saveThresholdTwoMessage( String message)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Threshold Two Message", message);

        editor.commit();
    }
    public void logIn(/*boolean success*/){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn",true);
    }

    //function to save language
    public void saveLanguage( String lang)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Language", lang);

        editor.commit();
    }

    //language getter
    public String languageReturn(){

        String lang = sharedPreferences.getString("Language", null);
        if(lang==null)
            return "en";
        else
            return lang;
    }


    public void logOut(/*boolean success*/){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn",false);
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

    //functions to get the messages set for each thresholds
    public String ThresholdOneMessageReturn()
    {
        String ThresholdMessage = sharedPreferences.getString("Threshold One Message", null);

        return ThresholdMessage;
    }


    public String ThresholdTwoMessageReturn()
    {
        String ThresholdMessage = sharedPreferences.getString("Threshold Two Message", null);

        return ThresholdMessage;
    }
    public boolean setTutorialSeen(boolean flag){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("wasTutorialSeen",flag);
        return editor.commit();
    }
    public boolean getTutorialSeen(){
        return sharedPreferences.getBoolean("wasTutorialSeen",false);
    }
    public boolean hasLoggedIn(){

        return sharedPreferences.getBoolean("loggedIn",false);
    }

    //in order to pass several time values, because bundles is not working
    public void saveTime(String hours, String minutes, String seconds)
    {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Hours", hours);
        editor.putString("Minutes",minutes);
        editor.putString("Seconds",seconds);

        editor.commit();

    }

    public Integer ReturnHoursSet()
    {
       Integer Hours = Integer.parseInt(sharedPreferences.getString("Hours", null));

        return Hours;
    }

    public Integer ReturnMinutesSet()
    {
        Integer Minutes = Integer.parseInt(sharedPreferences.getString("Minutes", null));

        return Minutes;

    }

    public Integer ReturnSecondsSet()
    {
        Integer Seconds = Integer.parseInt(sharedPreferences.getString("Seconds", null));

        return Seconds;

    }



    public void saveCheckInAddress(String address)
    {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("CheckInAddress", address);

        editor.commit();

    }


    public String returnCheckInAddress()
    {

        String tempCheckinAddress = sharedPreferences.getString("CheckInAddress", null);

        return tempCheckinAddress;
    }

    //1 corresponds to a message send
    //0 corresonds to send the "im safe message"
    public void setMessageSent(int input){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("messageSent", input);

        editor.commit();

    }

    //if theres nothing 2 will be returned
    public int getMessageSent(){
        int output = sharedPreferences.getInt("messageSent",2 );

        return output;
    }

    //functions used for the Check in activity
    public void resetTimerValue( Boolean value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("Reset Timer Value", value);

        editor.commit();

    }

    public Boolean getresetTimerValue()
    {
        boolean value = sharedPreferences.getBoolean("Reset Timer Value", false);

        return value;

    }

    public void firstTimerDoneService( Boolean value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("First Timer Done", value);

        editor.commit();

    }

    public Boolean getfirstTimerDoneService()
    {
        boolean value = sharedPreferences.getBoolean("First Timer Done", false);

        return value;

    }

    public void iAmSafe( Boolean value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("I Am Safe", value);

        editor.commit();

    }

    public Boolean getiAmSafe()
    {
        boolean value = sharedPreferences.getBoolean("I Am Safe", false);

        return value;

    }

    public void disableAudio(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("audioenable", false);
        editor.commit();

    }

    public void enableAudio(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("audioenable", true);
        editor.commit();

    }

    public boolean audioCheck(){
        boolean value = sharedPreferences.getBoolean("audioenable", true);

        return value;

    }




    public void recordingStart(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("recording", true);
        editor.commit();

    }

    public void recordingStop(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("recording", false);
        editor.commit();
    }


    public boolean checkifrecording(){
        boolean value = sharedPreferences.getBoolean("recording", false);
        return value;
    }


}