package com.example.findmytag.algorithms.knn;

import android.net.wifi.WifiManager;

import com.example.findmytag.wifi.WiFiDataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KNN {
    private WifiManager wifiManager;
    public ArrayList<Integer> dataRssi = new ArrayList<>();
    public ArrayList<String>  dataBssid =new ArrayList<>();
    public String s;
    private WiFiDataManager wiFiDataManager=new WiFiDataManager(wifiManager,dataBssid,dataRssi);
    public String localizationAlogrithm() {
        s =wiFiDataManager.scanWifi();
        dataRssi = wiFiDataManager.dataRssi;
        dataBssid = wiFiDataManager.dataBssid;
        return s;
    }
}