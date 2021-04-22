package com.example.findmytag;

import com.example.findmytag.algorithms.neuralnetwork.CNNLocUtils;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCNNLocUtils {
    @Test
    public void testINDNormalizer() {
        float n = 100.0f;
        float difference = 95.0f;
        INDArray input = Nd4j.create(new float[]{1, -95, 0, -76, 2, -57, -19, -138, 3},
                new int[]{3, 3});
        INDArray output = CNNLocUtils.normalizeINDArray(input, n, difference);
        INDArray assertion = Nd4j.create(new float[]{0, 0, 100, 20, 0, 40, 80, 0, 0}, new int[]{3
                , 3});
        assertArrayEquals(new long[]{3, 3}, output.shape());
        assertEquals(assertion, output);
        assertTrue(BooleanIndexing.and(output, Conditions.greaterThanOrEqual(0.0f)));
        assertTrue(BooleanIndexing.and(output, Conditions.lessThanOrEqual(n)));
    }
}
