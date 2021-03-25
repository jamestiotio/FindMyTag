package com.example.findmytag.algorithms.randomforest;


import smile.data.DataFrame;
import smile.data.formula.Formula;

public class WifiValues {

    public static DataFrame data;
    public static Formula formula = Formula.lhs("play");

    public static double[][] x;
    public static int[] y;


}
