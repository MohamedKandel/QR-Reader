package com.example.qreader_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_SCREEN_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,Scanning.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIME);
    }
}