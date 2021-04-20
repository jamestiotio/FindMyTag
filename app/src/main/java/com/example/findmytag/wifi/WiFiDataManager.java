package com.example.findmytag.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class WiFiDataManager {
    private WifiManager wifiManager;
    public List<ScanResult> scanResults;
  //  public MainActivity activity;
    private Context mcontext;
    StringBuilder sb,a;

    public ArrayList<Integer> dataRssi = new ArrayList<>();
    public ArrayList<String>  dataBssid =new ArrayList<>();
    private final List<String> AP_LIST = Arrays.asList("18-64-72-CD-5F-EC",
            "18-64-72-CD-5F-E6", "18-64-72-CD-60-00", "18-64-72-CD-5F-BE", "18-64-72-CD-5F-C6",
            "18-64-72-CD-60-88", "18-64-72-CD-60-9A", "18-64-72-CD-5F-B8", "18-64-72-CD-5F-38",
            "18-64-72-CD-5F-F4", "18-64-72-CD-60-14", "18-64-72-CD-60-DE", "18-64-72-CD-60-4A",
            "18-64-72-CD-60-D6", "18-64-72-CD-60-56", "18-64-72-CD-60-36", "18-64-72-CD-65-E6",
            "18-64-72-CD-5F-8C", "18-64-72-CD-60-7A","18-64-72-CD-5F-06",
            "18-64-72-CD-60-AE", "18-64-72-CD-5F-C8", "18-64-72-CD-62-E0", "18-64-72-CD-60-8A",
            "18-64-72-CD-62-3C", "18-64-72-CD-62-40", "18-64-72-CD-62-74", "18-64-72-CD-5F-0E",
            "18-64-72-CD-53-48", "18-64-72-CD-64-34", "18-64-72-CD-65-C4", "18-64-72-CD-64-4C",
            "18-64-72-CD-62-54", "18-64-72-CD-62-78", "18-64-72-CD-62-80", "18-64-72-CD-62-82",
            "18-64-72-CD-61-38", "18-64-72-CD-53-A4", "18-64-72-CD-66-7C", "18-64-72-CD-52-AC",
            "18-64-72-CD-53-70", "18-64-72-CD-52-BC", "18-64-72-CD-64-AA", "18-64-72-CD-65-A6",
            "18-64-72-CD-66-8C", "18-64-72-CD-5F-54");

    public WiFiDataManager(Context context) {
        mcontext = context;
        wifiManager = (WifiManager) mcontext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //adapter = new ArrayAdapter<>(mcontext, android.R.layout.simple_list_item_1, arrayList);
    }

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

        boolean x = wifiManager.startScan();

        if(!x){
            Toast.makeText(mcontext,"Please Waiting",Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mcontext.startActivity(myIntent);
        }
        Toast.makeText(mcontext, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
        return s;

    }
    public List userScanWiFi(){
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(mcontext, "WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }


        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        sb = new StringBuilder();
        List<ScanResult> wifiList = wifiManager.getScanResults();
        for (ScanResult scanResult : wifiList) {
            //if(scanResult.level>=90){
            for(int i=0;i<AP_LIST.size(); i++){
                if (scanResult.BSSID==AP_LIST.get(i)) {
                    dataRssi.add(scanResult.level);
                    dataBssid.add(scanResult.BSSID);
                    break;

                }
            }

            //}

        }
        boolean x = wifiManager.startScan();
        if(!x){
            Toast.makeText(mcontext,"Please Waiting",Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mcontext.startActivity(myIntent);
        }
        Toast.makeText(mcontext, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
        return wifiList;
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
