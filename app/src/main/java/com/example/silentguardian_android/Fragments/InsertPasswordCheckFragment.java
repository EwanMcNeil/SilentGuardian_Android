package com.example.silentguardian_android.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.Activities.thresholdActivity;
import com.example.silentguardian_android.Activities.profileActivity;

import java.util.Locale;

public class InsertPasswordCheckFragment extends DialogFragment {

    private static final String TAG = "PasswordCheck";

    protected EditText tempPassword;
    protected Button saveTempPasswordButton;

    protected SharePreferenceHelper sharePreferenceHelper;

    protected String inputIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_check_login_credentials, container, false);

        //Next lines assure activity uses the right language, otherwise some activities or fragment aren't fully catching up
        //Applying language start
        sharePreferenceHelper = new SharePreferenceHelper(getContext());
        String language = sharePreferenceHelper.languageReturn();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        //Applying language end
        inputIntent = getArguments().getString("intent");
        tempPassword = view.findViewById(R.id.editPasswordCheck);
        saveTempPasswordButton = view.findViewById(R.id.enterPasswordButton);


        sharePreferenceHelper = new SharePreferenceHelper(getContext());




        saveTempPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempPasswordCheck = tempPassword.getText().toString();
                Log.d(TAG, "set temp password = " + tempPasswordCheck);

                Log.d(TAG, "password = " + sharePreferenceHelper.passwordReturn());


                //authentication statement, upon success will allow access to the threshold activity
                if (tempPasswordCheck.equals(sharePreferenceHelper.passwordReturn())) {
                    Log.d(TAG, "entered if statement");

                    Intent intent;
                    switch (inputIntent) {
                        case "bluetooth":
                            intent = new Intent(getActivity(), BluetoothMainActivity.class);
                            Log.d(TAG, "blue");
                            startActivity(intent);
                            dismiss();
                            return;
                        case "profile":
                            intent = new Intent(getActivity(), profileActivity.class);
                            startActivity(intent);
                            dismiss();
                            return;
                        case "threshold":
                            intent = new Intent(getActivity(), thresholdActivity.class);
                            startActivity(intent);
                            dismiss();
                            return;
                        case "enableAudio":
                            SharePreferenceHelper helper = new SharePreferenceHelper(getContext());
                            if (helper.audioCheck() == true) {
                                helper.disableAudio();
                                Toast.makeText(getContext(), R.string.audioDisable, Toast.LENGTH_LONG).show();
                            } else {
                                helper.enableAudio();
                                Toast.makeText(getContext(), R.string.audioEnable, Toast.LENGTH_LONG).show();
                            }
                        default:
                            dismiss();

                    }
                    Log.d(TAG, "compared temp password to stored password");
                }

                //if authentication fails, keeps them stuck until correct password is entered...
                else {
                    Toast.makeText(getContext(), R.string.authentification, Toast.LENGTH_LONG).show();
                    //getDialog().dismiss();
                }


            }
        });

        return view;
    }
}
