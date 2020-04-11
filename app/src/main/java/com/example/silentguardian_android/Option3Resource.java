package com.example.silentguardian_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Option3Resource extends AppCompatActivity {

    protected TextView phone1_option3;   /**1-888-933-9007*/
    protected TextView phone2_option3;   /**1-888-595-5580*/
    protected TextView phone3_option3;   /**1-800-595-5580*/
    protected TextView phone4_option3;   /**1-800-363-9010*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option3_resource);

        phone1_option3 = findViewById(R.id.phone1_option3);
        phone2_option3 = findViewById(R.id.phone2_option3);
        phone3_option3 = findViewById(R.id.phone3_option3);
        phone4_option3 = findViewById(R.id.phone4_option3);


        /**activating first phone number*/
        phone1_option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:18889339007"));

                /**Must check permission*/
                if (ActivityCompat.checkSelfPermission(Option3Resource.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Option3Resource.this, new String[]{Manifest.permission.CALL_PHONE},10);
                    return;
                }

                /**if you do have a permission*/
                else {
                    try{    startActivity(callIntent);
                    }
                    catch(android.content.ActivityNotFoundException ex){
                        Toast.makeText(getApplicationContext(),"cannot find your activity",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**activating second phone number*/
        phone2_option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:18885955580"));

                /**Must check permission*/
                if (ActivityCompat.checkSelfPermission(Option3Resource.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Option3Resource.this, new String[]{Manifest.permission.CALL_PHONE},10);
                    return;
                }

                /**if you do have a permission*/
                else {
                    try{    startActivity(callIntent);
                    }
                    catch(android.content.ActivityNotFoundException ex){
                        Toast.makeText(getApplicationContext(),"cannot find your activity",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**activating third phone number*/
        phone3_option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:18005955580"));

                /**Must check permission*/
                if (ActivityCompat.checkSelfPermission(Option3Resource.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Option3Resource.this, new String[]{Manifest.permission.CALL_PHONE},10);
                    return;
                }

                /**if you do have a permission*/
                else {
                    try{    startActivity(callIntent);
                    }
                    catch(android.content.ActivityNotFoundException ex){
                        Toast.makeText(getApplicationContext(),"cannot find your activity",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**activating forth phone number*/
        phone4_option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:18003639010"));

                /**Must check permission*/
                if (ActivityCompat.checkSelfPermission(Option3Resource.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Option3Resource.this, new String[]{Manifest.permission.CALL_PHONE},10);
                    return;
                }

                /**if you do have a permission*/
                else {
                    try{    startActivity(callIntent);
                    }
                    catch(android.content.ActivityNotFoundException ex){
                        Toast.makeText(getApplicationContext(),"cannot find your activity",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
