package com.example.findmytag.algorithms.randomforest;

/**
 * This is to test Random Forest Algorithm specifically for our use case of WiFi values
 */

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.file.Path;

import smile.data.CategoricalEncoder;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.io.Read;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WifiValues {

    public static DataFrame data;
    public static Formula formula = Formula.lhs("play");

    public static double[][] level;
    public static double[][] dummy;
    public static double[][] onehot;
    public static String[] y;

    static{
        try{
            Path pathToFile = java.nio.file.Paths.get("/Users","zen","Downloads", "weather.nominal.csv");

            data = Read.csv(pathToFile);


//            level = formula.x(data).toArray(false, CategoricalEncoder.LEVEL);
//            dummy = formula.x(data).toArray(false, CategoricalEncoder.DUMMY);
//            onehot = formula.x(data).toArray(false, CategoricalEncoder.ONE_HOT);
            y = formula.y(data).toStringArray();

        } catch (Exception ex) {
            System.err.println("Failed to load 'WiFi Values': " + ex);
            System.exit(-1);
        }
    }
}
