package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Option1Resource extends AppCompatActivity {

    protected TextView url1_option1;
    protected TextView url2_option1;
    protected TextView url3_option1;
    protected TextView url4_option1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option1_resource);

    url1_option1 = findViewById(R.id.url1_option1);
    url2_option1 = findViewById(R.id.url2_option1);
    url3_option1 = findViewById(R.id.url3_option1);
    url4_option1 = findViewById(R.id.url4_option1);








    }
}
