package com.example.silentguardian_android.helpLinks;

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

import com.example.silentguardian_android.R;

/**
 * RESOURCE FOR MISSING PERSON
 */
public class Option2Resource extends AppCompatActivity {

    protected TextView phone1_option2;
    protected TextView phone2_option2;
    protected TextView url1_option2;
    /**
     * https://www.canadianhumantraffickinghotline.ca
     */
    protected TextView url2_option2;

    /**
     * http://www.afpad.ca
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option2_resource);

        phone1_option2 = findViewById(R.id.phone1_option2);
        phone2_option2 = findViewById(R.id.phone2_option2);
        url1_option2 = findViewById(R.id.url1_option2);
        url2_option2 = findViewById(R.id.url2_option2);


        /**activating first phone number*/

        phone1_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:18339001010"));

                /**Must check permission*/
                if (ActivityCompat.checkSelfPermission(Option2Resource.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Option2Resource.this, new String[]{Manifest.permission.CALL_PHONE}, 10);
                    return;
                }

                /**if you do have a permission*/
                else {
                    try {
                        startActivity(callIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "cannot find your activity", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**activating second phone number*/
        phone2_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:18774840404"));

                /**Must check permission*/
                if (ActivityCompat.checkSelfPermission(Option2Resource.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Option2Resource.this, new String[]{Manifest.permission.CALL_PHONE}, 10);
                    return;
                }

                /**if you do have a permission*/
                else {
                    try {
                        startActivity(callIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "cannot find your activity", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**Activating 1st url*/
        url1_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicking("https://www.canadianhumantraffickinghotline.ca");
            }
        });
        /**Activating 2nd url*/
        url2_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicking("http://www.afpad.ca");
            }
        });


    }

    public void clicking(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
