package com.example.findmytag.utils;

import com.example.findmytag.algorithms.randomforest.ResultGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class DataParser {
    String data = "";
    private static final String CSV_FILE_PATH
            = "./result.csv";


    //Updated variables after called readFile(file)
    private String nameOfSSIDs = "";
    private String onlyLevelsString = "";
    private String onlyCoordString = "";
    private String stringOfBSSID = "";
    private List<String> listOfBothSSIDs = new ArrayList<>();
    private List<String> listOfBSSIDs = new ArrayList<>();
    private List<String> listOfRSSI = new ArrayList<>();
    private List<String> listOfCoord = new ArrayList<>();
    private List<String> listOfRSSIs = new ArrayList<>();




    //For removing dump and extracting levels, coordinates at the same time
    private String[] tempArr;
    private String rssiAndCoordString = "";
    private String kay = "";
    private String stringBSSIDandRSSI = "";
    private String stringBSSID = "";
    private String stringRSSI = "";
    private String stringCoord = "";
    private int numOfLinesOfData = 0;


    public void readFile(File file) throws IOException {
        String[] arrOfRSSI;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s;
        while ((s = br.readLine()) != null) {
            numOfLinesOfData ++;
            data = data + s+ "\n";
            //For SSID names, in case needed
            if(s.contains(" - ")) {
                int index = s.indexOf(" - ");
                if (index > 0) {
                    nameOfSSIDs = nameOfSSIDs + s.substring(0, index) + "\n";
                    stringBSSIDandRSSI = stringBSSIDandRSSI + s.substring(index + 3) + "\n";

                } else {
                    nameOfSSIDs += "**********HIDDEN SSID**********\n";
                }
            }
        }

        getSubsInDelimeters(data);

        arrOfRSSI = data.split(" - ", -2);

        parseForLevelsAndXY(arrOfRSSI);
    }

    //for bssid and ssid and levels logic error
    public void getSubsInDelimeters(String str) throws IOException {
        String t = "";
        BufferedReader b = new BufferedReader(new StringReader(data));
        while((t = b.readLine()) != null){
            if(t.contains("=")){
                int boo = t.indexOf("=");
                stringCoord = t.substring(boo + 1, t.indexOf(")")+1);
                listOfCoord.add(stringCoord);
            }
        }

        // Stores the indices of
        Stack<Integer> dels = new Stack<Integer>();
        for(int i = 0; i < str.length(); i++) {

            // If opening delimeter
            // is encountered
            if (str.charAt(i) == '[') {
                dels.add(i);
            }

            // If closing delimeter
            // is encountered
            else if (str.charAt(i) == ']' && !dels.isEmpty()) {
                // Extract the position
                // of opening delimeter
                int pos = dels.peek();

                dels.pop();

                // Length of subString
                int len = i - 1 - pos;

                // Extract the subString
                String ans = str.substring(pos + 1, pos + 1 + len);

                listOfBothSSIDs.add(ans);
            }
        }
        BufferedReader bufReader = new BufferedReader(new StringReader(listOfBothSSIDs.toString()));
        String s = "";
        int count = 0;
        while ((s = bufReader.readLine()) != null) {
            count++;
            if(s.contains(" - ")) {
                int index = s.indexOf(" - ");
                stringBSSID = stringBSSID + s.substring(index+3).substring(0, s.substring(index+3).indexOf(" "))+";";
                stringRSSI = stringRSSI + s.substring(s.indexOf("- -")).substring(2)+";";
                //System.out.println(stringRSSI);
            }
            if(s.contains(",")){
                listOfBSSIDs.add(stringBSSID);
                stringBSSID = "";
                listOfRSSI.add(stringRSSI);
                stringRSSI="";
            }else if(count == numOfLinesOfData){
                stringRSSI = stringRSSI.substring(0, stringRSSI.indexOf("]"));
                listOfBSSIDs.add(stringBSSID);
                listOfRSSI.add(stringRSSI);
            }
        }

        for(String m : listOfRSSI){
            if(m.contains(",")){
                int iopo = m.indexOf(",");
                listOfRSSIs.add(m.substring(0, iopo));
            }else{
                listOfRSSIs.add(m);
            }
        }


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

    /**
     * List version of each getter.
     * @return List
     */



    public List<String> getSSID(){
        String str[] = getSSIDinString().split("\n", -2);
        List<String> ls = new ArrayList<String>();
        ls = Arrays.asList(str);
        return ls;
    }

    public List<String> getLevels(){
        return listOfRSSIs;
    }

    public List<String> getCoord(){
//        String str[] = getCoordInString().split("\n", -2);
//        List<String> ls = Arrays.asList(str);
//        return ls;
        return listOfCoord;
    }

    public List<String> getBSSID(){
        return listOfBSSIDs;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        File f = new File("/Users/zen/Downloads/WiFiData.txt");
        DataParser o = new DataParser();
        o.readFile(f);

        ResultGenerator.addDataToCSV(o.getBSSID(),o.getLevels(),o.getCoord(), CSV_FILE_PATH);









    }

}
