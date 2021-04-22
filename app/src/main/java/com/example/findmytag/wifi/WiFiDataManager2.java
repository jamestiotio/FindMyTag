//package com.example.findmytag.wifi;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiManager;
//import android.provider.Settings;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//public class WiFiDataManager2 {
//    private WifiManager wifiManager;
//    public List<ScanResult> scanResults;
//    //  public MainActivity activity;
//    private Context mcontext;
//    public HashMap<String,Integer> sorteddata= new HashMap<>();
//
//
//    public ArrayList<Integer> dataRssi = new ArrayList<>();
//    public ArrayList<String>  dataBssid =new ArrayList<>();
//
//    public WiFiDataManager2(Context context) {
//        mcontext = context;
//        wifiManager = (WifiManager) mcontext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        ;
//    }
//
//    public WiFiDataManager2() {
//
//    }
//
//    public HashMap scanWifi() {
//
//        if (!wifiManager.isWifiEnabled()) {
//            Toast.makeText(mcontext, "WiFi is disabled ... enabling it now", Toast.LENGTH_LONG).show();
//            wifiManager.setWifiEnabled(true);
//        }
//
//        mcontext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        wifiManager.startScan();
//
//        boolean x = wifiManager.startScan();
//
//        if (!x) {
//            Toast.makeText(mcontext, "Please Waiting", Toast.LENGTH_LONG).show();
//            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            mcontext.startActivity(myIntent);
//        }
//        Toast.makeText(mcontext, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
//        List<ScanResult> wifiList = wifiManager.getScanResults();
//        for (ScanResult scanResult : wifiList) {
//            //if(scanResult.level>=90){
//            for (int i = 0; i < AP_LIST.size(); i++) {
//                if (scanResult.BSSID == AP_LIST.get(i)) {
//                    sorteddata.put(scanResult.BSSID, scanResult.level);
//                    break;
//                }
//            }
//        }
//        return sorteddata;
//    }
//    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            List<ScanResult> wifiList = wifiManager.getScanResults();
//            for (ScanResult scanResult : wifiList) {
//                //if(scanResult.level>=90){
//                for(int i=0;i<AP_LIST.size(); i++){
//                    if (scanResult.BSSID==AP_LIST.get(i)) {
//                        sorteddata.put(scanResult.BSSID,scanResult.level);
//                        break;
//
//
//                    }
//                }
//
//            }
//
//
//
//
//            context.unregisterReceiver(this);
//
//
//
//        }
//    };
//
//}
