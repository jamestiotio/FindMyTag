package com.example.findmytag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Use this to parse data and input into our algorithm.
 *
 * To use, create an instance of this class with text file as an input.
 *
 * Output format: String, each item separated by NEWLINE (temporarily, working on switch to ArrayList)
 *
 * Only have Levels and Coordinate points at the moment.
 */
public class DataParserAlgos {
    File f = new File("/Users/zen/Downloads/WiFiData.txt");
    String data = "";



    //For removing dump and extracting levels, coordinates at the same time
    String[] arrOfRSSI;
    String[] tempArr;
    String rssiAndCoordString = "";
    String kay = "";


    public void readFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s;
        while ((s = br.readLine()) != null) {
            data = data + "\n" + s;
        }
        arrOfRSSI = data.split(" - ", -2);
    }

    public void parseForLevelsAndXY(){
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
            //for(int i = 0; i < rssiAndCoordString.length(); i++){
            if ((c == '-') || Character.isDigit(c) ||
                    (c == '(') || (c == ')') ||
                    (c == ',') || (c == '.')) {
                kay = kay + c;
            }
        }

    }

    public String getLevelsInString(){
        String onlyLevelsString = "";
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
        String onlyCoordString = "";
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

    public String getBSSIDInString(){
        String stringOfBSSID = "";
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





}
