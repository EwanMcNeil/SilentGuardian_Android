package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ThresholdSettingActivity extends AppCompatActivity {

    protected TextView thresholdEditText;
    protected int thresholdVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold_setting);
        thresholdEditText = findViewById(R.id.thresholdEditText);
        thresholdVal = getIntent().getIntExtra("THRESHOLDVAL", 0);


    }

    @Override
    protected void onStart() {
        super.onStart();
        thresholdVal = getIntent().getIntExtra("THRESHOLDVAL", 0);

        thresholdEditText.setText("Contacts in Threshold:" + thresholdVal);
    }
}
