package com.example.silentguardian_android.helpLinks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.silentguardian_android.R;

public class ResourceActivity extends AppCompatActivity {

    protected Button option1Button;
    protected Button option2Button;
    protected Button option3Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

    option1Button = findViewById(R.id.option1Button);
    option2Button = findViewById(R.id.option2Button);
    option3Button = findViewById(R.id.option3Button);

    option1Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ResourceActivity.this, Option1Resource.class);
            startActivity(intent);
        }
    });

    option2Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ResourceActivity.this, Option2Resource.class);
            startActivity(intent);

        }
    });

    option3Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ResourceActivity.this, Option3Resource.class);
            startActivity(intent);

        }
    });

    }
}
