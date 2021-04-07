package com.example.findmytag.wifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;


import com.example.findmytag.MainActivity;



public class WiFiDataManager {
    private WifiManager wifiManager;
    public List<ScanResult> scanResults;
    private volatile static WiFiDataManager wiFiDataManager;
    public MainActivity activity;
    private Context mcontext;

    public ArrayList<HashMap<Integer, Integer>> dataRssi = new ArrayList<HashMap<Integer, Integer>>(); // 每行代表一个Wifi热点，对应一个map，map的第一个值是数据的index，第二个值是rssi
    public HashMap<String, Integer> dataBssid = new HashMap<String, Integer>();
    public ArrayList<String> dataWifiNames = new ArrayList<String>();
    public int dataCount = 0;

    public WiFiDataManager(Context context) {
        mcontext = context;
        wifiManager = (WifiManager) mcontext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //adapter = new ArrayAdapter<>(mcontext, android.R.layout.simple_list_item_1, arrayList);
    }


    public void scanWifi() {

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(mcontext, "WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }


        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        boolean x = wifiManager.startScan();
        if(!x){
            Toast.makeText(mcontext,"Please Waiting",Toast.LENGTH_LONG).show();
//            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            mcontext.startActivity(myIntent);
        }
        Toast.makeText(mcontext, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();

    }

    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scanResults = wifiManager.getScanResults();
            context.unregisterReceiver(this);



        }
    };
}