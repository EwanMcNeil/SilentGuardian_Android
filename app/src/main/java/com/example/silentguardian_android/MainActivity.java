package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.silentguardian_android.Bluetooth.BluetoothMainActivity;

public class MainActivity extends AppCompatActivity {

    protected Button profileButton;
    protected Button thresholdButton;
    protected Button bubbleButton;
    protected Button mBluetoothButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileButton = findViewById(R.id.profileButton);
        thresholdButton = findViewById(R.id.thresholdButton);
        bubbleButton = findViewById(R.id.bubbleButton);
        mBluetoothButton = findViewById(R.id.BLEbutton);
        mBluetoothButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BluetoothMainActivity.class);
                startActivity(intent);
            }
        });

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
