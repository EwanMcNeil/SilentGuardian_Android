package com.example.silentguardian_android;

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
                    Intent intent = new Intent(getActivity(), ThresholdSettingActivity.class);
                    getActivity().startActivity(intent);
                    Log.d(TAG, "compared temp password to stored password");
                }

                //if authentication fails, escorts user back to the MainActivity...
                else {
                    Toast.makeText(getContext()," Authentication Failed: Incorrect Password ", Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
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
