package com.example.qreader_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar();
        RunAd();

        ArrayList<String> text = new ArrayList<>();
        text.add("QReader\n            ©2021 QReader");
        text.add("Version\n            1.0.0");
        TextView tv = null;
        text.add("Contact US\n         Galaxy Code Team");
        ListView lv = findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(About.this,android.R.layout.simple_list_item_1,text);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==2){
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    String[] TO = {"Galaxycode439@gmail.com"};
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                    }catch (Exception e){
                        Toast.makeText(About.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    private void ActionBar(){
        getSupportActionBar().setTitle("About Us");
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