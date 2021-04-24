package com.example.findmytag.algorithms.knn;

import android.net.wifi.WifiManager;

import com.example.findmytag.utils.DataParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KNN {
    public ArrayList<Integer> index = new ArrayList<>();
    public ArrayList<Integer> index2 = new ArrayList<>();
    public ArrayList<Integer> index3 = new ArrayList<>();
    public ArrayList<Integer> index4 = new ArrayList<>();
    public ArrayList<Integer> index5 = new ArrayList<>();
    public ArrayList<Integer> index6 = new ArrayList<>();
    public ArrayList<Integer> index7 = new ArrayList<>();
    public ArrayList<Integer> integerList = new ArrayList<>();
    public String key1, key2, key3, key4, key5, key6, key7;
    public DataParser dataParser;
    public ArrayList<String> hcbssid;
    private WifiManager wifiManager;

    public List<String> localizationAlogrithm(HashMap<String, Integer> sorteddata) throws IOException {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/download");
        String pathName = android.os.Environment.getExternalStorageDirectory() + "/download/";
        File file = new File(pathName, "WiFiData.txt");
        dataParser.readFile(file);
        String bssid = new String();
        List<String> rssi = dataParser.getLevels();
        String s = "";
        // 6:4e:f0;18:64:72:56:5a:77;18:64:72:56:5a:76;18:64:72:56:5a:73;18:64:72:56:5a:72;
        // 18:64:72:56:5a:71;18:64:72:56:4a:b7;18:64:72:56:4a:b6;18:64:72:56:4a:b3;
        // 18:64:72:56:4a:b1;18:64:72:56:4a:b0;18:64:72:56:5c:47;18:64:72:56:5c:46;
        // 18:64:72:56:5c:43;18:64:72:56:25:43;18:64:72:56:25:41;18:64:72:56:5c:41;
        // 18:64:72:56:58:c2;18:64:72:56:36:97;18:64:72:56:36:96;18:64:72:56:65:57;
        // 18:64:72:56:36:93;18:64:72:56:44:d7;18:64:72:56:44:d6;18:64:72:56:44:d3;
        // 18:64:72:56:44:d2;18:64:72:56:44:d1;18:64:72:56:36:92;18:64:72:56:36:91;
        // 18:64:72:55:35:37;18:64:72:55:35:36;18:64:72:55:35:33;18:64:72:55:35:32;
        // 18:64:72:55:35:31;18:64:72:55:35:30;18:64:72:56:64:d7;18:64:72:56:64:d6;
        // 18:64:72:56:64:d3;18:64:72:56:64:d2;18:64:72:56:64:d0;d4:6e:0e:21:8b:70;
        // 18:64:72:56:64:d1;18:64:72:56:23:d6;18:64:72:56:23:d3;18:64:72:56:23:d2;
        // 18:64:72:56:23:d1;18:64:72:56:42:e7;18:64:72:56:42:e6;18:64:72:56:42:e3;
        // 18:64:72:56:42:e2;18:64:72:56:42:e1;18:64:72:56:42:e0;18:64:72:56:25:57;
        // 18:64:72:56:25:56;18:64:72:56:25:53;18:64:72:56:25:52;18:64:72:56:4a:a3;
        // 18:64:72:56:25:51;18:64:72:56:66:57;18:64:72:56:42:f7;18:64:72:56:42:f3;
        // 18:64:72:56:66:53;18:64:72:56:42:f6;18:64:72:56:42:f2;18:64:72:56:42:f1;
        // 18:64:72:56:42:f0;18:64:72:55:f0:f7;18:64:72:56:59:b7;18:64:72:56:59:b6;
        // 18:64:72:56:59:b3;18:64:72:56:59:b2;18:64:72:56:59:b0;18:64:72:56:43:57;
        // 18:64:72:56:43:56;b0:95:75:f5:60:7f;18:64:72:56:43:53;18:64:72:56:43:52;
        // 18:64:72:56:5a:66;18:64:72:56:5a:63;18:64:72:56:5a:61;18:64:72:56:44:c3;
        // 18:64:72:56:68:c7;18:64:72:56:68:c6;18:64:72:56:68:c2;18:64:72:56:68:c3;
        // 18:64:72:56:68:c1;]
        for (String i : rssi) {
            for (int a = 0; a < i.length(); a++) {
                if (Character.isDigit(i.charAt(a))) {
                    s += i.charAt(a);
                }
            }

        }
        int sum = 0;
        int per = 2;
        int total = s.length();
        int nums = total / per;
        for (int index = 0; index < nums; index++) {
            String child = s.substring(index * per, (index + 1) * per);
            int a = Integer.parseInt(child);
            integerList.add(a);
        }
        List<String> x = dataParser.getCoordX();
        List<String> y = dataParser.getCoordY();
        List<String> z = dataParser.getCoordZ();
        List<String> coord = dataParser.getCoord();
        HashMap<String, Double> compare = new HashMap<>();
        int n = 0;
        for (String key : sorteddata.keySet()) {
            if (sorteddata.size() >= 7) {
                n += 1;
                if (n == 1) {
                    for (int i = 0; i < bssid.length(); i++) {
                        int at = bssid.indexOf(key, i);
                        i = at;
                        index.add(at / 18 - 1);
                        key1 = key;
                        if (at < 0) {
                            break;
                        }

                    }
                }
                if (n == 2) {
                    for (int i = 0; i < bssid.length(); i++) {
                        int at = bssid.indexOf(key, i);
                        i = at;
                        index2.add(at);
                        key2 = key;
                        if (at < 0) {
                            break;
                        }

                    }
                }
                if (n == 3) {
                    for (int i = 0; i < bssid.length(); i++) {
                        int at = bssid.indexOf(key, i);
                        i = at;
                        index3.add(at);
                        key3 = key;
                        if (at < 0) {
                            break;
                        }

                    }
                }
                if (n == 4) {
                    for (int i = 0; i < bssid.length(); i++) {
                        int at = bssid.indexOf(key, i);
                        i = at;
                        index4.add(at);
                        key4 = key;
                        if (at < 0) {
                            break;
                        }

                    }
                }
                if (n == 5) {
                    for (int i = 0; i < bssid.length(); i++) {
                        int at = bssid.indexOf(key, i);
                        i = at;
                        index5.add(at);
                        key5 = key;
                        if (at < 0) {
                            break;
                        }

                    }
                }
                if (n == 6) {
                    for (int i = 0; i < bssid.length(); i++) {
                        int at = bssid.indexOf(key, i);
                        i = at;
                        index6.add(at);
                        key6 = key;
                        if (at < 0) {
                            break;
                        }

                    }
                }
                if (n == 7) {
                    for (int i = 0; i < bssid.length(); i++) {
                        int at = bssid.indexOf(key, i);
                        i = at;
                        index7.add(at);
                        key7 = key;
                        if (at < 0) {
                            break;
                        }

                    }
                }
                if (n == 8) {
                    break;
                }

            }
        }
        for (int i = 0; i < 10; i++) {
            double dif =
                    Math.pow(integerList.get(index.get(i)) - sorteddata.get(key1), 2) + Math.pow(integerList.get(index2.get(i)) - sorteddata.get(key2), 2) + Math.pow(integerList.get(index3.get(i)) - sorteddata.get(key3), 2) + Math.pow(integerList.get(index4.get(i)) - sorteddata.get(key4), 2) + Math.pow(integerList.get(index5.get(i)) - sorteddata.get(key5), 2) + Math.pow(integerList.get(index6.get(i)) - sorteddata.get(key6), 2) + Math.pow(integerList.get(index7.get(i)) - sorteddata.get(key7), 2);
            String xyz = coord.get(i);
            compare.put(xyz, dif);
        }
        Collection<Double> set = compare.values();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        Set set2 = compare.entrySet();
        ArrayList<String> arr = new ArrayList<String>();
        Iterator it = set2.iterator();
        for (int i = 0; i < 8; i++) {
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getValue().equals(obj[obj.length - 1 - i])) {
                    String q = (String) entry.getKey();
                    arr.add(q);

                }

            }

        }
        return arr;
    }

}