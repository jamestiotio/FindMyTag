package com.example.findmytag.algorithms.neuralnetwork;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jetbrains.annotations.NotNull;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;
import org.nd4j.linalg.primitives.Triple;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

// Take note that by convention, a M x N matrix has M rows and N columns (i.e, height is M and
// width is N)
public class CNNLocUtils {
    // Calculate Pearson Correlation Coefficient between each column of normalized RSSI values of
    // the Kth AP at all N locations and the location vector (x or y coordinate).
    public static double calculatePCCMagnitude(INDArray w, INDArray l) {
        double[] x = w.toDoubleVector();
        double[] y = l.toDoubleVector();

        double rawCorrelation = new PearsonsCorrelation().correlation(x, y);

        // If NaN, assume no correlation
        if (Double.isNaN(rawCorrelation)) return 0.0d;

        // We only care about the magnitude
        return Math.abs(rawCorrelation);
    }

    // Calculate Hadamard Product between the normalized RSSI fingerprint array at the Nth
    // location (R) and the vector of correlation values (PCC).
    // Both matrices need to have equal number of rows and columns.
    // Purpose is to promote the impact of APs that are highly correlated to fingerprinted
    // locations.
    // The actual 1 x K matrix will be reshaped into a p x p matrix. If K is not a perfect square
    // number, we pad with zeros at the end.
    public static INDArray getHP(INDArray r, INDArray c) {
        INDArray temp = r.muli(c);

        int upperBound =
                (int) Math.ceil(Math.sqrt(WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size()));
        float[] transitory = temp.toFloatVector();

        // Pad the rest at the end with zeros
        for (int i = WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size(); i < upperBound * upperBound; i++) {
            transitory = ArrayUtils.add(transitory, 0);
        }

        INDArray padded = Nd4j.create(transitory, new int[]{upperBound, upperBound});

        return padded;
    }

    /**
     * Save a single channel INDArray as a INDArray equivalent of a grayscale image. We skip the
     * conversion of the resulting image into a file to save time. Output is a 2-dimensional
     * array (not obeying and following the standard procedure for image
     * classification in Deeplearning4j) because splitTestAndTrain only supports up to a features
     * rank of 4.
     * <p>
     * There's also NativeImageLoader.asMat(INDArray) and we can then use OpenCV to save it as an
     * image file.
     * <p>
     * Modified from https://github.com/sjsdfg/dl4j-tutorials/blob/master/src/main/java/lesson6
     * /UsingModelToPredict.java
     *
     * @param array input (2-dimensional square matrix array)
     * @return grayscale conversion (2-dimensional array)
     */
    public static INDArray imageFromHPINDArray(INDArray array) {
        long[] shape = array.shape();

        int height = (int) shape[0];
        int width = (int) shape[1];

        INDArray output = Nd4j.create(height, width);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gray = array.getInt(y, x);

                // Handle out of bounds pixel values
                gray = Math.min(gray, 255);
                gray = Math.max(gray, 0);

                // Grayscale image only has 1 band/channel
                output.putScalar(new int[]{y, x}, gray);
            }
        }
        return output;
    }

    // Parse CSV file input to get meaningful data and convert them into INDArrays.
    // Returns a triple of inputs and target outputs to serve as dataset.
    public static Triple<INDArray, INDArray, INDArray> parseCSV(String filePath) throws IOException {
        String[][] bssidList = new String[][]{};
        String[][] rssiList = new String[][]{};
        int[] xList = new int[]{};
        int[] yList = new int[]{};

        // TODO
        int maxWidth = 800;
        int maxHeight = 575;

        int[][] outerTransitory = new int[][]{};
        int[] innerTransitory = new int[WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size()];

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> r = reader.readAll();
            for (String[] item : r) {
                bssidList = ArrayUtils.add(bssidList, item[0].split(";"));
                rssiList = ArrayUtils.add(rssiList, item[1].split(";"));
                String[] coordinate = item[2].split(",");
                // Set target values with desired data types (100 classes for each row)
                float x = Float.parseFloat(item[2]);
                float y = Float.parseFloat(item[3]);
                int floor = Integer.parseInt(item[4]);
                xList = ArrayUtils.add(xList, normalizeFloat(x, 100, maxWidth));
                yList = ArrayUtils.add(yList, normalizeFloat(y, 100, maxHeight));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("CSV file does not exist!");
        } catch (CsvException e) {
            e.printStackTrace();
            throw new IOException("CSV file does not exist!");
        }

        // Loop through N locations
        for (int i = 0; i < bssidList.length; i++) {
            for (int j = 0; j < bssidList[i].length; j++) {
                // Set loop to go through all known K Wi-Fi AP BSSID list to impose fixed ordering
                for (int k = 0; k < WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size(); k++) {
                    // Extract only relevant BSSID and RSSI values
                    if (bssidList[i][j].equalsIgnoreCase(WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.get(k))) {
                        innerTransitory[k] = Integer.parseInt(rssiList[i][j]);
                        // Only select first occurrence for that coordinate (throw away duplicates)
                        break;
                    }
                }
            }
            innerTransitory = normalizeIntArray(innerTransitory, 100, 55);
            outerTransitory = ArrayUtils.add(outerTransitory, innerTransitory);
            // Reset innerTransitory
            innerTransitory = new int[WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size()];
        }

        // This is the desired N x K matrix (each "entry"/row will map to its own image fingerprint)
        INDArray normalizedFingerprints = Nd4j.createFromArray(outerTransitory);

        // Output of x coordinates
        INDArray xINDArray = Nd4j.createFromArray(xList);

        // Output of y coordinates
        INDArray yINDArray = Nd4j.createFromArray(yList);

        return new Triple<>(normalizedFingerprints, xINDArray, yINDArray);
    }

    // Normalize the RSSI scale from between -95 dB (lowest) and -40 dB (highest) to between 0 and N.
    // Utilizes custom implementation, slightly different from the ImagePreProcessingScaler class
    // in ND4J.
    public static INDArray normalizeINDArray(@NotNull INDArray RSSIArray, float n, float difference) {
        INDArray output = RSSIArray.dup();
        output.addi(95);
        output.divi(difference);
        output.muli(n);
        // Set all RSSI values > 0 to 0 to remove them since they are invalid/null values
        BooleanIndexing.replaceWhere(output, 0.0f, Conditions.greaterThan(n));
        // Set all RSSI values < minimum to 0 to remove them since they are invalid/null values
        BooleanIndexing.replaceWhere(output, 0.0f, Conditions.lessThan(0.0f));
        return output;
    }

    public static int[] normalizeIntArray(int[] array, float n, float difference) {
        int[] copy = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            float temp = (((float) array[i] + 95) / difference) * n;
            copy[i] = Math.round(temp);   // Rounding
        }

        return copy;
    }

    public static int normalizeFloat(float val, float n, int maxDimension) {
        if (val > maxDimension) return Math.round(n-1);
        if (val < 0) return 0;
        float temp = (val / maxDimension) * n;
        return Math.round(temp-1);    // Rounding
    }

    public static INDArray parseTestingCSV(String filePath) {
        String[][] bssidList = new String[][]{};
        String[][] rssiList = new String[][]{};

        int[][] outerTransitory = new int[][]{};
        int[] innerTransitory = new int[WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size()];

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> r = reader.readAll();
            for (String[] item : r) {
                bssidList = ArrayUtils.add(bssidList, item[0].split(";"));
                rssiList = ArrayUtils.add(rssiList, item[1].split(";"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        // Loop through N locations
        for (int i = 0; i < bssidList.length; i++) {
            for (int j = 0; j < bssidList[i].length; j++) {
                // Set loop to go through all known K Wi-Fi AP BSSID list to impose fixed ordering
                for (int k = 0; k < WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size(); k++) {
                    // Extract only relevant BSSID and RSSI values
                    if (bssidList[i][j].equalsIgnoreCase(WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.get(k))) {
                        innerTransitory[k] = Integer.parseInt(rssiList[i][j]);
                        // Only select first occurrence for that coordinate (throw away duplicates)
                        break;
                    }
                }
            }
            innerTransitory = normalizeIntArray(innerTransitory, 100, 55);
            outerTransitory = ArrayUtils.add(outerTransitory, innerTransitory);
            // Reset innerTransitory
            innerTransitory = new int[WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size()];
        }

        // This is the desired N x K matrix fingerprint
        INDArray normalizedFingerprints = Nd4j.createFromArray(outerTransitory);

        return normalizedFingerprints;
    }
}
