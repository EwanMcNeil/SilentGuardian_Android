package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

/**RESOURCE FOR MISSING PERSON*/
public class Option2Resource extends AppCompatActivity {

    protected TextView phone1_option2;
    protected TextView phone2_option2;
    protected TextView url1_option2;
    protected TextView url2_option2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option2_resource);

        phone1_option2 = findViewById(R.id.phone1_option2);
        phone2_option2 = findViewById(R.id.phone2_option2);
        url1_option2 = findViewById(R.id.url1_option2);
        url2_option2 = findViewById(R.id.url2_option2);




    }
}
