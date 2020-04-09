package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class Option3Resource extends AppCompatActivity {

    protected TextView phone1_option3;
    protected TextView phone2_option3;
    protected TextView phone3_option3;
    protected TextView phone4_option3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option3_resource);

        phone1_option3 = findViewById(R.id.phone1_option3);
        phone2_option3 = findViewById(R.id.phone2_option3);
        phone3_option3 = findViewById(R.id.phone3_option3);
        phone4_option3 = findViewById(R.id.phone4_option3);


    }
}
