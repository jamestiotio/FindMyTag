package com.example.findmytag.algorithms.randomforest;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.apache.commons.csv.CSVFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.PrintWriter;

import java.nio.file.Path;
import java.nio.file.Paths;

import tech.tablesaw.api.*;

import smile.base.cart.SplitRule;
import smile.classification.DataFrameClassifier;
import smile.classification.SoftClassifier;
import smile.data.CategoricalEncoder;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.glm.model.Model;
import smile.io.CSV;
import smile.classification.RandomForest;
import smile.validation.ClassificationMetrics;
import smile.validation.CrossValidation;
import smile.validation.LOOCV;
import smile.validation.metric.ClassificationMetric;


public class WiFiRF {
    public static DataFrame train;
    public static DataFrame test;
    public static Formula formula = Formula.lhs("location");


    //For building model
    int numberOfFolds = 5;
    int bestFold = 0;
    double bestAccuracy = 0d;
    SoftClassifier<double[]> bestModel = null;

    //Hyperparameters to be edited
    private static final int nTrees = 100;
    private static final int mTry = 2;
    private static final int maxDepth = 20;
    private static final int maxNodes = 100;
    private static final int nodeSize = 5;
    private static final double samplingRate = 1.0;



    public static double[][] x;
    public static double[] y;
    public static double[][] testX;
    public static double[] testY;
    static String path = "/result.csv" ;
    static String csvPath = android.os.Environment.getExternalStorageDirectory() + "/downloads" + path;
    static StructType schema = DataTypes.struct(
            new StructField("bssid", DataTypes.StringType),
            new StructField("rssi", DataTypes.StringType),
            new StructField("coordinates", DataTypes.StringType)
    );

//
//    //Try 1
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void trainModel(String trainingPath, String outputPath, int responseIndex, boolean skipCrossValidation) throws Exception{
//
//
//        String modelFilePath = Paths.get(outputPath, "RandomForest.model").toString();
//
//        //Write temp file
//        Path csvFilePath = Paths.get(csvPath);
//        FileInputStream fis = new FileInputStream(csvFilePath.toFile());
//
//
//
////        CSVParser csvParser = new CSVParser();
//
//    }
//
//
//    //Try 2: Fail
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static void trainModel(){
//        CSV csv = new CSV(CSVFormat.DEFAULT);
//        csv.schema(schema);
//
//        File root = android.os.Environment.getExternalStorageDirectory();
//        String pathName = root.getAbsolutePath()+"/download"+path;

        //Build the model
//        CrossValidation cv = new CrossValidation()


//        try{
//            train = csv.read(pathName);
//            test = csv.read(pathName);
//
//            x = formula.x(train).toArray(false, CategoricalEncoder.DUMMY);
//            y = formula.y(train).toDoubleArray();
//            testX = formula.x(test).toArray(false, CategoricalEncoder.DUMMY);
//            testY = formula.y(test).toDoubleArray();
//
//            //Build the model
//            RandomForest model = RandomForest.fit(formula, train, nTrees, mTry, maxDepth, maxNodes, nodeSize, samplingRate);
//
//            double[] importance = model.importance();
//            for(int i = 0 ; i < importance.length ; i++){
//                System.out.format("%-15s %.4f%n", model.schema().name(), importance[i]);
//            }
//
//            ClassificationMetrics metrics = LOOCV.classification(formula, train,
//                    (f, x) -> (DataFrameClassifier) RandomForest.fit(f, x, nTrees,mTry,maxDepth,maxNodes,nodeSize,samplingRate));
//
//
//            File file = new File(pathName,"WiFiData.txt");
//            final String TAG = "MODEL";
//
//
//            try {
//                FileOutputStream f = new FileOutputStream(file);
//                PrintWriter pw = new PrintWriter(f);
//                pw.println(model);
//                pw.flush();
//                pw.close();
//                f.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Log.i(TAG, "File not found.");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }catch(Exception e){
//            System.err.println("Failed to load WiFi values: "+ e);
//            e.printStackTrace();
//            System.exit(-1);
//        }
//
//
//    }
//
//
//    //Try 3
//
//    public static void trainModelRF() throws IOException {
//
//        //Get the data
//        Table t = Table.read().csv(csvPath);
//
//        //Filter data to start
//        Table[] splits = t.sampleSplit(0.5);
//        Table train = splits[0];
//        Table test = splits[1];
//
//        //RandomForest rf = RandomForest.fit()
//
//    }


}