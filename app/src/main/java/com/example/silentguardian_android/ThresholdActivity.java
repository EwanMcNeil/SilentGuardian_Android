package com.example.silentguardian_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ThresholdActivity extends AppCompatActivity{

    protected Button oneButton;
    protected Button twoButton;
    protected Button threeButton;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold);

        oneButton = findViewById(R.id.thresholdOneButton);
        twoButton = findViewById(R.id.thresholdTwoButton);
        threeButton = findViewById(R.id.thresholdThreeButton);

        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextActivity(1);
            }
        });

        twoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity(2);
            }
        });

        threeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity(3);
            }
        });
    }
    private void nextActivity(int i){
        Intent intent = new Intent(ThresholdActivity.this, ThresholdSettingActivity.class);
        intent.putExtra("THRESHOLDVAL", i);
        startActivity(intent);

    }
}
