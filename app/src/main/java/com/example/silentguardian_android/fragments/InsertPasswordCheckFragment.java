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

import com.example.silentguardian_android.Bluetooth.BluetoothMainActivity;
import com.example.silentguardian_android.Database.SharePreferenceHelper;
import com.example.silentguardian_android.MainActivity;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.ThresholdSettingActivity;
import com.example.silentguardian_android.profileActivity;

public class InsertPasswordCheckFragment extends DialogFragment {

    private static final String TAG = "PasswordCheck";

    protected EditText tempPassword;
    protected Button saveTempPasswordButton;

    protected SharePreferenceHelper sharePreferenceHelper;

    protected String inputIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_check_login_credentials, container,false);

        inputIntent = getArguments().getString("intent");
        tempPassword = view.findViewById(R.id.editPasswordCheck);
        saveTempPasswordButton = view.findViewById(R.id.enterPasswordButton);


        sharePreferenceHelper = new SharePreferenceHelper(getContext());



        Toast.makeText(getContext(),inputIntent, Toast.LENGTH_LONG).show();


        saveTempPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempPasswordCheck = tempPassword.getText().toString();
                Log.d(TAG, "set temp password = " + tempPasswordCheck);

                Log.d(TAG, "password = " + sharePreferenceHelper.passwordReturn());



                //authentication statement, upon success will allow access to the threshold activity
                if(tempPasswordCheck.equals(sharePreferenceHelper.passwordReturn())){
                    Log.d(TAG, "entered if statement");

                    Intent intent;
                    switch (inputIntent){
                        case "bluetooth":
                            intent = new Intent(getActivity(), BluetoothMainActivity.class);
                            Log.d(TAG, "blue");
                            startActivity(intent);
                            return;
                        case "profile":
                            intent = new Intent(getActivity(), profileActivity.class);
                            startActivity(intent);
                            return;
                        case "threshold":
                            intent = new Intent(getActivity(), ThresholdSettingActivity.class);
                            startActivity(intent);
                            return;
                        default:
                            dismiss();

                    }
                    Log.d(TAG, "compared temp password to stored password");
                }

                //if authentication fails, keeps them stuck until correct password is entered...
                else {
                    Toast.makeText(getContext()," Authentication Failed: Incorrect Password, Try Again ", Toast.LENGTH_LONG).show();
                    //getDialog().dismiss();
                }



            }
        });

        return view;
    }
}
