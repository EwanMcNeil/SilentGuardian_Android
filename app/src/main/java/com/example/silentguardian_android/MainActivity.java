package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.silentguardian_android.Database.SharePreferenceHelper;

public class MainActivity extends AppCompatActivity {

    protected Button profileButton;
    protected Button thresholdButton;
    protected Button bubbleButton;
    protected SharePreferenceHelper sharePreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileButton = findViewById(R.id.profileButton);
        thresholdButton = findViewById(R.id.thresholdButton);
        bubbleButton = findViewById(R.id.bubbleButton);
        sharePreferenceHelper = new SharePreferenceHelper(this);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , profileActivity.class);
                startActivity(intent);
            }
        });


       thresholdButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //checking if password macthes from user to sharedpreferences
              // if( sharePreferenceHelper.passwordReturn() ==  )
               InsertPasswordCheckFragment dialog = new InsertPasswordCheckFragment();

               dialog.show(getSupportFragmentManager(), "InsertPasswordCheck");


               // if (InsertPasswordCheckFragment.)

              // Intent intent = new Intent(MainActivity.this , ThresholdActivity.class);
               //startActivity(intent);
           }
       });

       bubbleButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this , BubbleActivity.class);
               startActivity(intent);
           }
       });


       //if the user doesn't have an account existing, this if statement takes them to profile activity to create first profile
        if(sharePreferenceHelper.userNameReturn() == null){
            Intent intent = new Intent(MainActivity.this , profileActivity.class);
            startActivity(intent);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


        // Upon resuming the mainActivity, if the user has a name saved into sharedpreferences, then replace the text on the profile button to their name.
        if(sharePreferenceHelper.userNameReturn()!= null) {


            profileButton.setText(sharePreferenceHelper.userNameReturn() + "'s Profile Page ");
        }
    }
}
