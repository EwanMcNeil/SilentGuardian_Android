package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected Button profileButton;
    protected Button thresholdButton;
    protected Button bubbleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileButton = findViewById(R.id.profileButton);
        thresholdButton = findViewById(R.id.thresholdButton);
        bubbleButton = findViewById(R.id.bubbleButton);

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
               Intent intent = new Intent(MainActivity.this , ThresholdActivity.class);
               startActivity(intent);
           }
       });

       bubbleButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this , BubbleActivity.class);
               startActivity(intent);
           }
       });

    }


}
