package com.example.findmytag.algorithms.randomforest;

import org.apache.commons.csv.CSVFormat;

import smile.data.CategoricalEncoder;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.measure.NominalScale;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.io.CSV;
import smile.util.Paths;


public class WiFiRF {
    public static DataFrame train;
    public static DataFrame test;
    public static Formula formula = Formula.lhs("rings");

    public static double[][] x;
    public static double[] y;
    public static double[][] testX;
    public static double[] testY;

    static{
        StructType schema = DataTypes.struct(
                new StructField("bssid", DataTypes.StringType),
                new StructField("rssi", DataTypes.StringType),
                new StructField("signal strength", DataTypes.IntegerType)
        );

        CSV csv = new CSV(CSVFormat.DEFAULT);
        csv.schema(schema);

        try{
            //train = csv.read();
            //test = csv.read();

            x = formula.x(train).toArray(false, CategoricalEncoder.DUMMY);
            y = formula.y(train).toDoubleArray();
            testX = formula.x(test).toArray(false, CategoricalEncoder.DUMMY);
            testY = formula.y(test).toDoubleArray();

        }catch(Exception e){
            System.err.println("Failed to load WiFi values: "+ e);
            e.printStackTrace();
            System.exit(-1);
        }

    }

}