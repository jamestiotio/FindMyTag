//package com.example.findmytag.algorithms.randomforest;
//
//import com.example.findmytag.algorithms.randomforest.classification.AbstractClassifier;
//
//import com.example.findmytag.algorithms.randomforest.classification.ConfusionMatrix;
//import com.example.findmytag.algorithms.randomforest.classification.IntConvertibleColumn;
//import com.example.findmytag.algorithms.randomforest.classification.StandardConfusionMatrix;
//import com.google.common.base.Preconditions;
//
//import tech.tablesaw.api.NumberColumn;
//import tech.tablesaw.util.DoubleArrays;
//
//import java.util.SortedSet;
//import java.util.TreeSet;
//
//public class RandomForest extends AbstractClassifier {
//
//    private final smile.classification.RandomForest classifierModel;
//
//    private RandomForest(int nTrees, int[] classArray, NumberColumn... columns) {
//        double[][] data = DoubleArrays.to2dArray(columns);
//        this.classifierModel = new smile.classification.RandomForest(data, classArray, nTrees);
//    }
//
//    public static RandomForest learn(int nTrees, IntConvertibleColumn classes, NumberColumn... columns) {
//        int[] classArray = classes.toIntArray();
//        return new RandomForest(nTrees, classArray, columns);
//    }
//
//    public int predict(double[] data) {
//        return classifierModel.predict(data);
//    }
//
//    public ConfusionMatrix predictMatrix(IntConvertibleColumn labels, NumberColumn... predictors) {
//        Preconditions.checkArgument(predictors.length > 0);
//
//        SortedSet<Object> labelSet = new TreeSet<>(labels.asIntegerSet());
//        ConfusionMatrix confusion = new StandardConfusionMatrix(labelSet);
//
//        populateMatrix(labels.toIntArray(), confusion, predictors);
//        return confusion;
//    }
//
//    @Override
//    int predictFromModel(double[] data) {
//        return classifierModel.predict(data);
//    }
//}
