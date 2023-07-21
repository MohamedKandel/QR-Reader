package com.example.qreader_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class Scanning extends AppCompatActivity {

    CodeScanner scanner;
    CodeScannerView scan;
    TextView txt;
    Context context;
    int id;
    int id_2;
    DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        ActionBar();
        RunAd();

        context = this;
        db = new DBConnection(this);

        scan = findViewById(R.id.scan);
        txt = findViewById(R.id.txt);
        txt.setVisibility(View.INVISIBLE);
        scanner = new CodeScanner(this,scan);
        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String rs = result.getText();
                        if (rs.contains("https://") || rs.contains("http://")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rs));
                            id=0;
                            id = db.getData().size();
                            int max = id;
                            if (id!=0) {
                                max = db.get_maxID();
                                max++;
                            }
                            db.insetIntohistory(max,rs);
                            startActivity(browserIntent);


                            ClipboardManager clipboard = (ClipboardManager)
                                    getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData myClip = ClipData.newPlainText("", rs);
                            clipboard.setPrimaryClip(myClip);
                            Toast.makeText(Scanning.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                        }else {
                            id = 0;
                            id = db.getData().size();
                            int max = id;
                            if (id!=0) {
                                max = db.get_maxID();
                                max++;
                            }
                            db.insetIntohistory(max,rs);

                            txt.setVisibility(View.VISIBLE);
                            txt.setText(rs);

                            ClipboardManager clipboard = (ClipboardManager)
                                    getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData myClip = ClipData.newPlainText("", rs);
                            clipboard.setPrimaryClip(myClip);
                            Toast.makeText(Scanning.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanner.startPreview();
                txt.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void ActionBar(){
        getSupportActionBar().setTitle("Scanning...");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.action_bar));
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

    @Override
    protected void onResume() {
        super.onResume();
        Request();
    }

    private void Request() {
        Dexter.withContext(Scanning.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(Scanning.this,"Camera Permission is required",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.options){
            //start options activity
            Intent intent = new Intent(Scanning.this,Options.class);
            startActivity(intent);
        }
        return true;
    }
}