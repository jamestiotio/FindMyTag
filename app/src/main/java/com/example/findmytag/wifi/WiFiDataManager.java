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
        Toast.makeText(mcontext, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scanResults = wifiManager.getScanResults();
            context.unregisterReceiver(this);

            // for every result found via scanWifi, store the wifi SSID and approximate distance to it in an array

        }
    };
}
//    public static WiFiDataManager getInstance() {
//        if (wiFiDataManager == null) {
//            synchronized (WiFiDataManager.class) {
//                if (wiFiDataManager == null) {
//                    wiFiDataManager = new WiFiDataManager();
//                }
//            }
//        }
//        return wiFiDataManager;
//    }

//    public void init(MainActivity activity) {
//        this.activity = activity;
//        if (wifiManager == null) {
//            wifiManager = (WifiManager) activity.getApplicationContext()
//                    .getSystemService(Context.WIFI_SERVICE);
//        }
//        if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
//            Toast.makeText(activity, "正在开启wifi，请稍后...", Toast.LENGTH_SHORT)
//                    .show();
//            if (wifiManager == null) {
//                wifiManager = (WifiManager) activity.getApplicationContext()
//                        .getSystemService(Context.WIFI_SERVICE);
//            }
//            if (!wifiManager.isWifiEnabled()) {
//                wifiManager.setWifiEnabled(true);
//            }
//        }
//    }
//
//    public void startCollecting(MainActivity activity) {
//        wifiManager.startScan();
//
//        activity.registerReceiver(cycleWifiReceiver, new IntentFilter(
//                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//    }
//
//    private final BroadcastReceiver cycleWifiReceiver = new BroadcastReceiver() {
//        @SuppressLint("UseSparseArrays")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            scanResults = wifiManager.getScanResults();
//
//            // 更新热点列表，只增不减，顺序不变，同时将RSSI记录下来
//            for (int i = 0; i < scanResults.size(); i++) {
//                if (!dataBssid.containsKey(scanResults.get(i).BSSID)) { // 新增一个wifi热点
//                    dataBssid.put(scanResults.get(i).BSSID, dataBssid.size());
//                    dataWifiNames.add(scanResults.get(i).SSID);
//                    HashMap<Integer, Integer> tmp = new HashMap<Integer, Integer>();
//                    tmp.put(dataCount, scanResults.get(i).level);
//                    dataRssi.add(tmp);
//                } else { // wifi热点已存在
//                    dataRssi.get(dataBssid.get(scanResults.get(i).BSSID)).put(
//                            dataCount, scanResults.get(i).level);
//                }
//            }
//            dataCount++;
//
//
//            // 收到后开始下一次扫描，控制一下时间，每秒最多两次
//            wifiManager.startScan();
//
//        }
//    };
//
//    public void endCollecting(MainActivity activity) {
//        activity.unregisterReceiver(cycleWifiReceiver); // 取消监听
//
//        // 然后存储数据到文件
//        new FileManager().saveData();
//
//    }
//
//}