package com.example.findmytag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Use this to parse data and input into our algorithm.
 *
 * To use, create an instance of this class with text file as an input.
 *
 * Two diff output formats:
 * 1. String, each item separated by NEWLINE
 * 2. List
 *
 * Only have Levels and Coordinate points at the moment.
 *
 *
 * How To Use:
 * 1. Call readFile(file): Prepares text file to be broken into individual arrayLists, while conveniently gets SSID names
 * 2. Call getSSID(), getLevels(), getString(), getBSSID() to get the individual datas
 */
public class DataParserAlgos {
    //File f = new File("/Users/zen/Downloads/WiFiData.txt");
    String data = "";

    //Updated variables after called readFile(file)
    private String nameOfSSIDs = "";
    private String onlyLevelsString = "";
    private String onlyCoordString = "";
    private String stringOfBSSID = "";

    //For removing dump and extracting levels, coordinates at the same time
    private String[] tempArr;
    private String rssiAndCoordString = "";
    private String kay = "";

    public void readFile(File file) throws IOException {
        String[] arrOfRSSI;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s;
        while ((s = br.readLine()) != null) {
            data = data + s+ "\n";

            //For SSID names, in case needed
            if(s.contains(" - ")) {
                int index = s.indexOf(" - ");
                if (index > 0) {
                    nameOfSSIDs = nameOfSSIDs + s.substring(0, index) + "\n";
                } else {
                    nameOfSSIDs += "**********HIDDEN SSID**********\n";
                }
            }
        }
        arrOfRSSI = data.split(" - ", -2);

        parseForLevelsAndXY(arrOfRSSI);
    }

    public void parseForLevelsAndXY(String[] arrOfRSSI){
        for (String x : arrOfRSSI) {
            if (x.contains("=")) {
                tempArr = x.split(", ", -2);
                String[] filteredArr = new String[tempArr.length];
                for (int j = 0; j < tempArr.length; j++) {
                    if (tempArr[j].length() > 15) {
                        filteredArr[j] = tempArr[j];
                    }
                }
                for (String y : filteredArr) {
                    if (y != null) {
                        rssiAndCoordString = rssiAndCoordString + y + "\n";
                    }
                }
            }
        }
        for (char c : rssiAndCoordString.toCharArray()) {
            if ((c == '-') || Character.isDigit(c) ||
                    (c == '(') || (c == ')') ||
                    (c == ',') || (c == '.')) {
                kay = kay + c;
            }
        }
    }

    /**
     * String Versions of each getter.
     * @return String of each elements
     */

    public String getSSIDinString(){
        return nameOfSSIDs;
    }

    public String getLevelsInString(){
        for (int k = 0; k < kay.length(); k++) {
            if (((kay.charAt(k)) == '-') && Character.isDigit(kay.charAt(k + 2))) {
                onlyLevelsString = onlyLevelsString + kay.charAt(k) + kay.charAt(k + 1) + kay.charAt(k + 2) + "\n";
            }else if ( ((kay.charAt(k)) == '-') && Character.isDigit(kay.charAt(k + 2)) &&
                    Character.isDigit(kay.charAt(k + 3))) {
                onlyLevelsString = onlyLevelsString + kay.charAt(k) + kay.charAt(k + 1) + kay.charAt(k + 2) + kay.charAt(k + 3) + "\n";
            }
        }
        return onlyLevelsString;
    }

    public String getCoordInString(){
        for (int k = 0; k < kay.length(); k++) {
            if (k < kay.length()-2 && Character.isDigit(kay.charAt(k)) && kay.charAt(k+2) != '(' ) {
                if(k < kay.length()-1 && kay.charAt(k+1) != '(') {
                    onlyCoordString += kay.charAt(k);
                }
            }

            if (kay.charAt(k) == '(' || kay.charAt(k) == ')' || kay.charAt(k) == '.' || kay.charAt(k) == ',') {
                onlyCoordString += kay.charAt(k);
                if(kay.charAt(k) == ')' && k < kay.length()-1 && !Character.isDigit(kay.charAt(k+1)) ){
                    onlyCoordString += "\n";
                }
            }
        }
        return onlyCoordString;
    }

    /**
     * List version of each getter.
     * @return List
     */

    public String getBSSIDInString(){
        for(int o = 0; o < data.length(); o++){
            char boo = data.charAt(o);
            if(boo == ':'){
                stringOfBSSID += boo;
            }
            else if((o < data.length() -  2 && data.charAt(o+2) == ':') ||
                    (o < data.length() - 1 && data.charAt(o+1) == ':')) {
                stringOfBSSID += boo;

            }
            else if((o > 2 && data.charAt(o-2) == ':')|| ( o > 1 && data.charAt(o-1) == ':' )) {
                stringOfBSSID += boo;
                if(data.charAt(o-2) == ':' && data.charAt(o+1) == ' '){
                    stringOfBSSID += "\n";
                }
            }

        }
        return stringOfBSSID;
    }


    public List<String> getSSID(){
        String str[] = nameOfSSIDs.split("\n", -2);
        List<String> ls = new ArrayList<String>();
        ls = Arrays.asList(str);
        return ls;
    }

    public List<String> getLevels(){
        String str[] = onlyLevelsString.split("\n", -2);
        List<String> ls = Arrays.asList(str);
        return ls;
    }

    public List<String> getCoord(){
        String str[] = onlyCoordString.split("\n", -2);
        List<String> ls = Arrays.asList(str);
        return ls;
    }

    public List<String> getBSSID(){
        String str[] = stringOfBSSID.split("\n", -2);
        List<String> ls = Arrays.asList(str);
        return ls;
    }


}
