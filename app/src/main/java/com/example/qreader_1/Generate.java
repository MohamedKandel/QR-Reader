package com.example.qreader_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Generate extends AppCompatActivity {

    private final int REQUEST_CODE = 1;

    Button download,generate,delete;
    ImageView img;
    EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        ActionBar();
        ActivityCompat.requestPermissions(Generate.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        RunAd();
    }

    private void ActionBar(){
        getSupportActionBar().setTitle("Generate QR");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //to do some code if user accept permissions
                    download = findViewById(R.id.download);
                    delete = findViewById(R.id.delete);
                    generate = findViewById(R.id.generate);
                    img = findViewById(R.id.img);
                    txt = findViewById(R.id.txt);
                    //generating qr code
                    generate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String data = txt.getText()+"";
                            QRGEncoder qrgEncoder = new QRGEncoder(data,null, QRGContents.Type.TEXT,
                                    500);
                            try {
                                Bitmap bitmap = qrgEncoder.getBitmap();
                                img.setImageBitmap(bitmap);
                            }catch (Exception e){
                                Toast.makeText(Generate.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //save picture to device
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BitmapDrawable draw = (BitmapDrawable) img.getDrawable();
                            Bitmap bitmap = draw.getBitmap();
                            String name = txt.getText()+"";

                            FileOutputStream outStream = null;
                            String path = Environment.getExternalStorageDirectory().getPath();
                            File file = new File(path + "/QReader");
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            String fileName;

                            if (name.contains("www.")) {      //http://www.facebook.com
                                String str1 = name.substring(name.indexOf('.')+1,name.length());
                                name = str1.substring(0,str1.indexOf('.'));
                            }
                            fileName = String.format("%s.jpg",name);
                            File outFile = new File(file, fileName);
                            try {
                                outStream = new FileOutputStream(outFile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                                outStream.flush();
                                outStream.close();
                                Toast.makeText(Generate.this,file.getPath()+"/"+name+".jpg",Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                Toast.makeText(Generate.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //delete image from image view
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            img.setImageDrawable(null);
                        }
                    });

                }else {
                    Toast.makeText(Generate.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                return;
        }
    }

}