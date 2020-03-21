package com.example.silentguardian_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Database.SharePreferenceHelper;
import com.example.silentguardian_android.MainActivity;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.ThresholdSettingActivity;

public class InsertPasswordCheckFragment extends DialogFragment {

    private static final String TAG = "PasswordCheck";

    protected EditText tempPassword;
    protected Button saveTempPasswordButton;
    protected Button cancelButton;
    protected SharePreferenceHelper sharePreferenceHelper;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_check_login_credentials, container,false);

        tempPassword = view.findViewById(R.id.editPasswordCheck);
        saveTempPasswordButton = view.findViewById(R.id.enterPasswordButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        sharePreferenceHelper = new SharePreferenceHelper(getContext());




        saveTempPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempPasswordCheck = tempPassword.getText().toString();
                Log.d(TAG, "set temp password = " + tempPasswordCheck);

                Log.d(TAG, "password = " + sharePreferenceHelper.passwordReturn());



                //authentication statement, upon success will allow access to the threshold activity
                if(tempPasswordCheck.equals(sharePreferenceHelper.passwordReturn())){
                    Log.d(TAG, "entered if statement");


                    //for the purpose of simplicity, i am going to make a password check whenever someone enters the app. only once.
                    //going to change the second parameter of the intent from "thresholdActivity", to "MainActivity"
                    getDialog().dismiss();
                    Log.d(TAG, "compared temp password to stored password");
                }

                //if authentication fails, keeps them stuck until correct password is entered...
                else {
                    Toast.makeText(getContext()," Authentication Failed: Incorrect Password, Try Again ", Toast.LENGTH_LONG).show();
                    //getDialog().dismiss();
                }



            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             getDialog().dismiss();
            }
        });







        return view;
    }
}
