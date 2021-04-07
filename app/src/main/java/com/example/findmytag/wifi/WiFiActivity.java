package com.example.findmytag.wifi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.findmytag.LocationActivity;
import com.example.findmytag.MappingFragment;
import com.example.findmytag.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.util.Map;


public class WiFiActivity extends AppCompatActivity{
    private ListView wifiList;
    private WifiManager wifiManager;
    StringBuilder sb;
    String t1 ,path,txt;

    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;

    WifiReceiver receiverWifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        wifiList = findViewById(R.id.wifiList);
        Button buttonScan = findViewById(R.id.scanBtn);
        Button buttonBack = findViewById(R.id.backtolocation);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Turning WiFi ON...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        buttonBack.setOnClickListener(v -> {
            Intent x = new Intent(WiFiActivity.this, LocationActivity.class);
            startActivity(x);
        });

        buttonScan.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(WiFiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        WiFiActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION
                );
            } else {
                wifiManager.startScan();
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (wifiManager.getScanResults() != null) {
                                break;
                            }
                        }
                        //save to file
                        sb = new StringBuilder();
                        List<ScanResult> wifiList = wifiManager.getScanResults();
                        ArrayList<String> deviceList = new ArrayList<>();
                        for (ScanResult scanResult : wifiList) {
                            sb.append("\n").append(scanResult.SSID).append(" - ").append(scanResult.BSSID).append(" - ").append(scanResult.level);


                        }
                        HashMap txt=new HashMap();
                        txt.put(t1,sb);

                            try {
                                path =Environment.getDownloadCacheDirectory().toString() + File.separator +"hello.txt";

                                File fss = new File(path);
                                if(!fss.exists()){
                                    try{
                                        fss.mkdirs();
                                    }catch (Exception e){
                                        //
                                    }
                                }

                                //FileOutputStream outputStream =new FileOutputStream(fss);
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fss));
                                objectOutputStream.writeObject(txt);
                                new FileOutputStream(fss).close();
                                new FileOutputStream(fss).close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                        }


                });
                th.start();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        receiverWifi = new WifiReceiver(wifiManager, wifiList);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiverWifi, intentFilter);
        getWifi();
    }

    private void getWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(WiFiActivity.this, "version>=marshmallow", Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(WiFiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(WiFiActivity.this, "location turned off", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(WiFiActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                Toast.makeText(WiFiActivity.this, "location turned on", Toast.LENGTH_SHORT).show();
                wifiManager.startScan();
            }
        } else {
            Toast.makeText(WiFiActivity.this, "scanning", Toast.LENGTH_SHORT).show();
            wifiManager.startScan();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverWifi);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(WiFiActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    wifiManager.startScan();
                } else {

                    Toast.makeText(WiFiActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }


    public void SendMessageValue(float x, float y) {
        // TODO Auto-generated method stub
        t1 = "(" + x + "," + y + ")";

    }
}