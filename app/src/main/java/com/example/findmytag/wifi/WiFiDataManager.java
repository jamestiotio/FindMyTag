package com.example.findmytag.wifi;
import java.util.ArrayList;
import java.util.Arrays;
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
    StringBuilder sb;
    public HashMap<String,Integer> sorteddata= new HashMap<>();
    private final List<String> AP_LIST =Arrays.asList("SUTD_Wifi","SUTD_LAB","SUTD_Guest","eduroam");
    public WiFiDataManager(Context context) {
        mcontext = context;
        wifiManager = (WifiManager) mcontext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


    public WiFiDataManager(WifiManager wifiManager,ArrayList dataBssid,ArrayList dataRssi ){
        this.wifiManager=wifiManager;
        this.dataBssid=dataBssid;
        this.dataRssi=dataRssi;
    }

    public String scanWifi() {
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(mcontext, "WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        sb = new StringBuilder();
        List<ScanResult> wifiList = wifiManager.getScanResults();
        for (ScanResult scanResult : wifiList) {
            sb.append(("\n")+scanResult.SSID + " - " + scanResult.BSSID + " - " + scanResult.level);
        }


        String s=sb.toString();
        /*
        for (ScanResult scanResult : wifiList) {
            //if(scanResult.level>=90){
            for(int i=0;i<AP_LIST.size(); i++){
                if (scanResult.BSSID==AP_LIST.get(i)) {
                    dataRssi.add(scanResult.level);
                    dataBssid.add(scanResult.BSSID);
                    break;
                }
            }
        }
*/
        boolean x = wifiManager.startScan();

        if(!x){
            Toast.makeText(mcontext,"Please Wait",Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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

        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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
            if(scanResult.level<=-85) {
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

    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sb = new StringBuilder();
            List<ScanResult> wifiList = wifiManager.getScanResults();
            for (ScanResult scanResult : wifiList) {

                sb.append(("\n")+scanResult.SSID + " - " + scanResult.BSSID + " - " + scanResult.level);
            }
            ArrayList  arrayList=new ArrayList( Arrays.asList(sb.toString().split(",")));


            context.unregisterReceiver(this);



        }
    };
}