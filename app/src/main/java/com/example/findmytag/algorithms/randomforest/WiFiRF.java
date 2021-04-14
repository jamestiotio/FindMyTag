package com.example.findmytag.algorithms.randomforest;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.apache.commons.csv.CSVFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import smile.base.cart.SplitRule;
import smile.classification.DataFrameClassifier;
import smile.data.CategoricalEncoder;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.glm.model.Model;
import smile.io.CSV;
import smile.regression.RandomForest;
import smile.validation.ClassificationMetrics;
import smile.validation.LOOCV;
import smile.validation.metric.ClassificationMetric;


public class WiFiRF {
    public static DataFrame train;
    public static DataFrame test;
    public static Formula formula = Formula.lhs("location");

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
    static String path = "./result.csv" ;
    static StructType schema = DataTypes.struct(
            new StructField("bssid", DataTypes.StringType),
            new StructField("rssi", DataTypes.StringType),
            new StructField("coordinates", DataTypes.LongType)
    );


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void trainModel(String pathName){
        CSV csv = new CSV(CSVFormat.DEFAULT);
        csv.schema(schema);

        try{
            train = csv.read(pathName);
            test = csv.read(pathName);

            x = formula.x(train).toArray(false, CategoricalEncoder.DUMMY);
            y = formula.y(train).toDoubleArray();
            testX = formula.x(test).toArray(false, CategoricalEncoder.DUMMY);
            testY = formula.y(test).toDoubleArray();

            //Build the model
            RandomForest model = RandomForest.fit(formula, train, nTrees, mTry, maxDepth, maxNodes, nodeSize, samplingRate);

            double[] importance = model.importance();
            for(int i = 0 ; i < importance.length ; i++){
                System.out.format("%-15s %.4f%n", model.schema().name(), importance[i]);
            }

            ClassificationMetrics metrics = LOOCV.classification(formula, train,
                    (f, x) -> (DataFrameClassifier) RandomForest.fit(f, x, nTrees,mTry,maxDepth,maxNodes,nodeSize,samplingRate));

            //save the model in a file
//            Path temp = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                temp = Files.createTempFile("rf-test", ".tmp");
//            }
//            OutputStream file = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                file = Files.newOutputStream(temp);
//            }
//            ObjectOutputStream out = new ObjectOutputStream(file);
//            out.writeObject(model);
//            out.close();
//            file.close();
            File file = new File(pathName,"WiFiData.txt");
            final String TAG = "MODEL";


            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.println(model);
                pw.flush();
                pw.close();
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i(TAG, "File not found.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch(Exception e){
            System.err.println("Failed to load WiFi values: "+ e);
            e.printStackTrace();
            System.exit(-1);
        }


    }



}