package com.example.findmytag.algorithms.neuralnetwork;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import java.security.SecureRandom;
import org.nd4j.linalg.factory.Nd4j;

public class TestTrain {
    protected DataSet test;
    protected DataSet train;

    public TestTrain(DataSet input, int splitSize, SecureRandom rng) {
        int inTest = 0;
        int inTrain = 0;
        int testSize = input.numExamples() - splitSize;

        INDArray train_features = Nd4j.create(splitSize, input.getFeatures().columns());
        INDArray train_outcomes = Nd4j.create(splitSize, input.numOutcomes());
        INDArray test_features  = Nd4j.create(testSize, input.getFeatures().columns());
        INDArray test_outcomes  = Nd4j.create(testSize, input.numOutcomes());

        for (int i = 0; i < input.numExamples(); i++) {
            DataSet D = input.get(i);
            if (rng.nextDouble() < (splitSize-inTrain)/(double)(input.numExamples()-i)) {
                train_features.putRow(inTrain, D.getFeatures());
                train_outcomes.putRow(inTrain, D.getLabels());
                inTrain += 1;
            } else {
                test_features.putRow(inTest, D.getFeatures());
                test_outcomes.putRow(inTest, D.getLabels());
                inTest += 1;
            }
        }

        train = new DataSet(train_features, train_outcomes);
        test  = new DataSet(test_features, test_outcomes);
    }

    public DataSet getTrain() {
        return train;
    }

    public DataSet getTest() {
        return test;
    }
}