package com.example.findmytag.algorithms.randomforest;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.file.Path;

import smile.data.CategoricalEncoder;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.io.Read;
import smile.util.Paths;

import static smile.util.Paths.home;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WeatherNominal {

    public static DataFrame data;
    public static Formula formula = Formula.lhs("play");

    public static double[][] level;
    public static double[][] dummy;
    public static double[][] onehot;
    public static int[] y;

    static {
        try {
            //data = Read.arff(Paths.getTestData("weka/weather.nominal.arff"));
            //data = Read.arff(java.nio.file.Paths.get(home + "/data", "weka/weather.nominal.arff"));

            Path pathToFile = java.nio.file.Paths.get("/Users","zen","Downloads", "weather.nominal.arff.txt");
            data = Read.arff(pathToFile);


            level = formula.x(data).toArray(false, CategoricalEncoder.LEVEL);
            dummy = formula.x(data).toArray(false, CategoricalEncoder.DUMMY);
            onehot = formula.x(data).toArray(false, CategoricalEncoder.ONE_HOT);
            y = formula.y(data).toIntArray();
        } catch (Exception ex) {
            System.err.println("Failed to load 'weather nominal': " + ex);
            System.exit(-1);
        }
    }
}

