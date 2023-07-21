package com.example.qreader_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Options extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ActionBar();
        RunAd();
        ImageView img1,img2,img3,img4;
        img1 = findViewById(R.id.img1);
        img1.setImageResource(R.drawable.generate_2);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Options.this,Generate.class);
                startActivity(intent);
                //Toast.makeText(Options.this,"Generate QR Code",Toast.LENGTH_SHORT).show();
            }
        });

        img2 = findViewById(R.id.img2);
        img2.setImageResource(R.drawable.history);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Options.this,History.class);
                startActivity(intent);
                //Toast.makeText(Options.this,"History",Toast.LENGTH_SHORT).show();
            }
        });

        img3 = findViewById(R.id.img3);
        img3.setImageResource(R.drawable.scan);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Options.this,Scanning.class);
                startActivity(intent);
                //Toast.makeText(Options.this,"Scan",Toast.LENGTH_SHORT).show();
            }
        });

        img4 = findViewById(R.id.img4);
        img4.setImageResource(R.drawable.about);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Options.this,About.class);
                startActivity(intent);
                //Toast.makeText(Options.this,"About us",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ActionBar(){
        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.action_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void RunAd(){
        AdView adview;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
    }
}