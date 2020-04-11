package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Option1Resource extends AppCompatActivity {

    protected TextView url1_option1; /**https://cavac.qc.ca/en/*/
    protected TextView url2_option1; /**https://www.centredecrise.ca/lescentres*/
    protected TextView url3_option1; /**https://www.ivac.qc.ca*/
    protected TextView url4_option1; /**http://www.ccvt.org*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option1_resource);

    url1_option1 = findViewById(R.id.url1_option1);
    url2_option1 = findViewById(R.id.url2_option1);
    url3_option1 = findViewById(R.id.url3_option1);
    url4_option1 = findViewById(R.id.url4_option1);



        url1_option1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clicking("https://cavac.qc.ca/en/");
         }
         });

        url2_option1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clicking("https://www.centredecrise.ca/lescentres");
        }
        });

        url3_option1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clicking("https://www.ivac.qc.ca");
            }
        });

        url4_option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicking("http://www.ccvt.org");
            }
        });


        }

        public void clicking(String url){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
    }
}
