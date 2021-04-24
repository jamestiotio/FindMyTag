package com.example.findmytag.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

import com.example.findmytag.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class WiFiDataManager {
    private volatile static WiFiDataManager wiFiDataManager;
    private final List<String> AP_LIST = Arrays.asList("SUTD_Wifi", "SUTD_LAB", "SUTD_Guest",
            "eduroam");
    public List<ScanResult> scanResults;
    public MainActivity activity;
    public HashMap<String, Integer> sorteddata = new HashMap<>();
    StringBuilder sb;
    String s;
    private WifiManager wifiManager;
    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sb = new StringBuilder();
            List<ScanResult> wifiList = wifiManager.getScanResults();
            for (ScanResult scanResult : wifiList) {

                sb.append(("\n") + scanResult.SSID + " - " + scanResult.BSSID + " - " + scanResult.level);
            }
            ArrayList arrayList = new ArrayList(Arrays.asList(sb.toString().split(",")));


            context.unregisterReceiver(this);


        }
    };
    private Context mcontext;


    public WiFiDataManager(Context context) {
        mcontext = context;
        wifiManager =
                (WifiManager) mcontext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


    }

    public String scanWifi() {

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(mcontext, "WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }


        mcontext.registerReceiver(wifiReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        sb = new StringBuilder();
        List<ScanResult> wifiList = wifiManager.getScanResults();
        for (ScanResult scanResult : wifiList) {

            //    if (scanResult.SSID=="SU9B1716E0"||scanResult.SSID=="SUE612461D"||scanResult
            //    .SSID=="edAD0C4780"||scanResult.SSID=="SUC67E22ED"){
            sb.append(("\n") + scanResult.SSID + " - " + scanResult.BSSID + " - " + scanResult.level);
            //      }


        }
        s = sb.toString();

        boolean x = wifiManager.startScan();

        if (!x) {
            Toast.makeText(mcontext, "Please Wait", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mcontext.startActivity(myIntent);
        }
        Toast.makeText(mcontext, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
        return s;

    }

    public HashMap userScanWifi() {

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(mcontext, "WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        mcontext.registerReceiver(wifiReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        boolean x = wifiManager.startScan();

        if (!x) {
            Toast.makeText(mcontext, "Please Waiting", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mcontext.startActivity(myIntent);
        }
        Toast.makeText(mcontext, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
        List<ScanResult> wifiList = wifiManager.getScanResults();
        for (ScanResult scanResult : wifiList) {
            if (scanResult.level <= -85) {
                for (int i = 0; i < AP_LIST.size(); i++) {
                    if (scanResult.SSID.equals(AP_LIST.get(i))) {
                        sorteddata.put(scanResult.BSSID, scanResult.level);
                        break;
                    }
                }
            }
        }
        return sorteddata;
    }

    public HashMap<String, Integer> getsorteddata() {
        return sorteddata;

    }

    public String gets() {
        return s;
    }
}