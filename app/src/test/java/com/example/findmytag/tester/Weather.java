package com.example.findmytag.tester;

import android.os.Build;

import androidx.annotation.RequiresApi;

import smile.data.CategoricalEncoder;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.io.Read;

import static smile.util.Paths.home;

/**
 * @author Haifeng
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Weather {

    public static DataFrame data;
    public static Formula formula = Formula.lhs("play");

    public static double[][] x;
    public static int[] y;

    private static String filename = "/Users/zen/Downloads/weather.nominal.arff.txt";

    static {
        try {
//            Path pathToFile = java.nio.file.Paths.get(filename);
//            System.out.println(pathToFile.toAbsolutePath());

            data = Read.arff(java.nio.file.Paths.get(home + "/data", "weka/weather.arff"));

            //data = Read.arff(Paths.getTestData(filename));

            x = formula.x(data).toArray(false, CategoricalEncoder.DUMMY);
            y = formula.y(data).toIntArray();
        } catch (Exception ex) {
            System.err.println("Failed to load 'weather': " + ex);
            System.exit(-1);
        }
    }
}
