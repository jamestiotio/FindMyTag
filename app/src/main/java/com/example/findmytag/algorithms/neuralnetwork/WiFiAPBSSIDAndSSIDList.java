package com.example.findmytag.algorithms.neuralnetwork;

import java.util.Arrays;
import java.util.List;

public class WiFiAPBSSIDAndSSIDList {
    // Use these to only selectively identify and take into account the desired AP signals (instead
    // of other random BSSIDs that might come by and visit the proximity). The ordering of the
    // APs here dictates the specific hard-coded AP order for the entire neural network
    // implementation.
    public static final List<String> KNOWN_B2L1_AP_LIST = Arrays.asList("18:64:72:CD:5F:EC",
            "18:64:72:CD:5F:E6", "18:64:72:CD:60:00", "18:64:72:CD:5F:BE", "18:64:72:CD:5F:C6",
            "18:64:72:CD:60:88", "18:64:72:CD:60:9A", "18:64:72:CD:5F:B8", "18:64:72:CD:5F:38",
            "18:64:72:CD:5F:F4", "18:64:72:CD:60:14", "18:64:72:CD:60:DE", "18:64:72:CD:60:4A",
            "18:64:72:CD:60:D6", "18:64:72:CD:60:56", "18:64:72:CD:60:36", "18:64:72:CD:65:E6",
            "18:64:72:CD:5F:8C", "18:64:72:CD:60:7A");
    public static final List<String> KNOWN_B2L2_AP_LIST = Arrays.asList("18:64:72:CD:5F:06",
            "18:64:72:CD:60:AE", "18:64:72:CD:5F:C8", "18:64:72:CD:62:E0", "18:64:72:CD:60:8A",
            "18:64:72:CD:62:3C", "18:64:72:CD:62:40", "18:64:72:CD:62:74", "18:64:72:CD:5F:0E",
            "18:64:72:CD:53:48", "18:64:72:CD:64:34", "18:64:72:CD:65:C4", "18:64:72:CD:64:4C",
            "18:64:72:CD:62:54", "18:64:72:CD:62:78", "18:64:72:CD:62:80", "18:64:72:CD:62:82",
            "18:64:72:CD:61:38", "18:64:72:CD:53:A4", "18:64:72:CD:66:7C", "18:64:72:CD:52:AC",
            "18:64:72:CD:53:70", "18:64:72:CD:52:BC", "18:64:72:CD:64:AA", "18:64:72:CD:65:A6",
            "18:64:72:CD:66:8C", "18:64:72:CD:5F:54");
    // Use this SSID list as a filter, but store data according to BSSID (do remember that
    // BSSID/MAC address spoofing require more technical skill, although not impossible, as
    // compared to SSID name spoofing).
    public static final List<String> KNOWN_WIFI_SSID_LIST = Arrays.asList("SUTD_Wifi", "SUTD_LAB"
            , "SUTD_Guest", "eduroam");
    // Use this BSSID list to preserve ordering of pixel data assignment to the abstract image
    // representation (these are arbitrarily chosen from the numerous actual BSSIDs available at
    // the Campus Centre).
    public static final List<String> KNOWN_WIFI_BSSID_LIST = Arrays.asList("18:64:72:56:68:D7",
            "18:64:72:56:68:D6", "18:64:72:56:68:D3", "18:64:72:56:68:D2", "18:64:72:55:34:97",
            "18:64:72:55:34:96", "18:64:72:56:5E:72", "18:64:72:56:64:D3", "18:64:72:56:5E:73",
            "18:64:72:56:5E:77");
}
