package com.example.findmytag.algorithms.randomforest;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultGenerator {
    private static List<Triplet> listOfTriplet = new ArrayList<>();

    //TODO: Include Z value when training final model
    public static void addDataToCSV(List<String> BSSID, List<String> LEVELS, List<String> COORDX,
                                    List<String> COORDY, List<String> COORDZ, String output) throws IOException {
        File file = new File(output);
        FileWriter outputFile = new FileWriter(file);

        CSVWriter writer = new CSVWriter(outputFile);

        List<String[]> data = new ArrayList<>();
        //data.add(new String[] {"BSSID", "RSSI", "X", "Y", "Z"});

        for (int i = 0; i < LEVELS.size(); i++) {
            data.add(new String[]{BSSID.get(i), LEVELS.get(i), COORDX.get(i), COORDY.get(i),
                    COORDZ.get(i)});
        }
        writer.writeAll(data);
        writer.close();
    }

    public static void addDataToCSVWithoutCoord(List<String> BSSID, List<String> LEVELS,
                                                String output) throws IOException {
        File file = new File(output);
        FileWriter outputFile = new FileWriter(file);

        CSVWriter writer = new CSVWriter(outputFile);

        List<String[]> data = new ArrayList<>();
        //data.add(new String[] {"BSSID", "RSSI", "X", "Y", "Z"});

        for (int i = 0; i < LEVELS.size(); i++) {
            data.add(new String[]{BSSID.get(i), LEVELS.get(i)});
        }
        writer.writeAll(data);
        writer.close();
    }

//    public static void tupleGenerator(List<String> BSSID, List<String> LEVELS, List<String>
//    COORD){
//        for(int i = 0 ; i < LEVELS.size() ; i++){
//            listOfTriplet.add(new Triplet(BSSID.get(i), LEVELS.get(i), COORD.get(i)));
//        }
//    }
//    public static List<Triplet> getListOfTriplets(){
//        return listOfTriplet;
//    }
}

