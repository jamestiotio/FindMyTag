package com.example.findmytag.algorithms.neuralnetwork;

import android.graphics.PointF;

import org.apache.commons.lang3.ArrayUtils;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.CacheMode;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.primitives.Pair;
import org.nd4j.linalg.primitives.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is just a simple feed-forward convolutional neural network (Conv-ReLu CNN-LOC), nothing
 * fancy. This network uses RSSI instead of CSI information data to solve a fingerprinting
 * classification problem. Based on this research paper:
 * https://ieeexplore.ieee.org/document/8662548, using CSI data will result in relatively better and
 * more accurate localization results. However, no current smartphone API allows access to the Wi-Fi
 * AP's physical layer CSI information (this includes the Android API). Some level of hacking to the
 * lower layers could be executed but its legality is questionable. Manual offline mapping using the
 * Linux 802.11n CSI Tool (https://dhalperi.github.io/linux-80211n-csitool/) could be conducted but
 * this is not entirely in line with the project's requirements to actually conduct the mapping
 * on-the-fly on the smartphone mobile device itself. Alternatively, a more-recently published
 * academic paper (https://www.mdpi.com/2079-9292/10/1/2) proposed a HDLM-based model and
 * established that it is much more performant (in terms of both accuracy and training/inference
 * speed). However we decided against implementing the approach in said paper due to 2 main reasons:
 * - The decision to scale the rectangles indicating the different Wi-Fi APs following a somewhat
 * random ordering seems to be quite arbitrary.
 * - The scaling of the boxes would inevitably lead to an inherent bias that leans towards those
 * Wi-Fi APs later in the somewhat arbitrary ordering, and hence it is not that scalable to many and
 * much more Wi-Fi APs.
 * <p>
 * Regarding scalability issues, this network is scalable in terms of different buildings (with
 * different physical measurements such as the attenuation and propagation constants) and different
 * numbers/arrangements of Wi-Fi access points with different specifications/models, but this may
 * not generally scale to N floors (this is only for 2 floors).
 * <p>
 * This is a modified implementation of this research paper:
 * https://dl.acm.org/doi/10.1145/3194554.3194594
 */
public class NeuralNetwork {
    private static final Logger log = LoggerFactory.getLogger(NeuralNetwork.class);
    // Global variables to accept the classification results from the background thread
    public double x, y;
    public int floor;
    private static final int numOfEpochs = 12;
    private static final long seed = 1802;
    private static final int CLASSES_COUNT = 100;
    private static final int nChannels = 1;
    private static final int k = WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size();
    private int upperBound;
    private boolean saveUpdater = false;
    String resultFilePath;
    Triple<INDArray, INDArray, INDArray> parsedInput;
    INDArray xCorrelationVector;
    INDArray yCorrelationVector;
    INDArray xImages;
    INDArray yImages;
    DataSet xDataSet;
    DataSet yDataSet;
    // We separate the classifier for each coordinate axis
    MultiLayerNetwork xCoordClassifierNetwork;
    MultiLayerNetwork yCoordClassifierNetwork;

    // Initialize class
    public NeuralNetwork(String dataSetFilePath) {
        this.resultFilePath = dataSetFilePath;
        // Parse CSV file to prep for model training process
        this.parsedInput = CNNLocUtils.parseCSV(this.resultFilePath);

        this.xCorrelationVector = Nd4j.create(1, k);
        this.yCorrelationVector = Nd4j.create(1, k);

        for (int i = 0; i < k; i++) {
            INDArray w = this.parsedInput.getFirst().getColumn(i);
            double xPCC = CNNLocUtils.calculatePCCMagnitude(w, this.parsedInput.getSecond());
            this.xCorrelationVector.putScalar(new int[]{0, i}, xPCC);
            double yPCC = CNNLocUtils.calculatePCCMagnitude(w, this.parsedInput.getThird());
            this.yCorrelationVector.putScalar(new int[]{0, i}, yPCC);
        }

        int n = (int) this.parsedInput.getFirst().shape()[0];
        this.upperBound = (int) Math.ceil(Math.sqrt(k));

        this.xImages = Nd4j.create(n, this.upperBound, this.upperBound);
        this.yImages = Nd4j.create(n, this.upperBound, this.upperBound);

        for (int i = 0; i < n; i++) {
            float[] r = this.parsedInput.getFirst().getRow(i).toFloatVector();
            INDArray floatR = Nd4j.create(r, new int[]{1, k});
            INDArray xHP = CNNLocUtils.getHP(floatR, this.xCorrelationVector);
            INDArray yHP = CNNLocUtils.getHP(floatR, this.yCorrelationVector);
            INDArray xImage = CNNLocUtils.imageFromHPINDArray(xHP);
            INDArray yImage = CNNLocUtils.imageFromHPINDArray(yHP);
            xImages.put(i, xImage);
            yImages.put(i, yImage);
        }

        INDArray xInputs = this.xImages.reshape(n, 1, this.upperBound, this.upperBound);
        INDArray yInputs = this.yImages.reshape(n, 1, this.upperBound, this.upperBound);

        INDArray xTargets = Nd4j.create(n, CLASSES_COUNT);
        INDArray yTargets = Nd4j.create(n, CLASSES_COUNT);

        int[] tempX = this.parsedInput.getSecond().reshape(n, 1).toIntVector();
        int[] tempY = this.parsedInput.getThird().reshape(n, 1).toIntVector();

        for (int i = 0; i < n; i++) {
            INDArray x = Nd4j.zeros(CLASSES_COUNT);
            x.putScalar(tempX[i], 1.0f);
            xTargets.putRow(i, x);

            INDArray y = Nd4j.zeros(CLASSES_COUNT);
            y.putScalar(tempY[i], 1.0f);
            yTargets.putRow(i, y);
        }

        this.xDataSet = new DataSet(xInputs, xTargets);
        this.yDataSet = new DataSet(yInputs, yTargets);
    }

    // Load pre-trained models for each classifier (default filepaths are trained_x_model.zip and trained_y_model.zip)
    public NeuralNetwork(String xClassifierModel, String yClassifierModel) {
        try {
            MultiLayerNetwork xModel = ModelSerializer.restoreMultiLayerNetwork(xClassifierModel);
            MultiLayerNetwork yModel = ModelSerializer.restoreMultiLayerNetwork(yClassifierModel);
            this.xCoordClassifierNetwork = xModel;
            this.yCoordClassifierNetwork = yModel;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void train() {
        log.info("Shuffling data...");
        this.xDataSet.shuffle();
        // splitTestAndTrain only supports up to a features rank of 4
        SplitTestAndTrain xTestAndTrain = this.xDataSet.splitTestAndTrain(0.80);
        DataSet xTrainingData = xTestAndTrain.getTrain();
        DataSet xTestData = xTestAndTrain.getTest();

        this.yDataSet.shuffle();
        // splitTestAndTrain only supports up to a features rank of 4
        SplitTestAndTrain yTestAndTrain = this.yDataSet.splitTestAndTrain(0.80);
        DataSet yTrainingData = yTestAndTrain.getTrain();
        DataSet yTestData = yTestAndTrain.getTest();

        log.info("Building model...");
        // Create the CNN model
        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .activation(Activation.RELU)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(0.01))
                .l2(5 * 1e-4)
                .cacheMode(CacheMode.NONE)
                .trainingWorkspaceMode(WorkspaceMode.ENABLED)
                .inferenceWorkspaceMode(WorkspaceMode.ENABLED)
                .cudnnAlgoMode(ConvolutionLayer.AlgoMode.PREFER_FASTEST)
                .convolutionMode(ConvolutionMode.Same)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer) // Normalize to prevent vanishing or exploding gradients
                .list()
                // Block 1
                .layer(0, new ConvolutionLayer.Builder()
                        // nIn and nOut specify depth. nIn here is the nChannels and nOut is the number of filters to be applied
                        .name("cnn1")
                        .cudnnAlgoMode(ConvolutionLayer.AlgoMode.PREFER_FASTEST)
                        .kernelSize(2, 2)
                        .stride(1, 1)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.RELU)
                        .nIn(nChannels)    // Grayscale image as input (one channel)
                        .nOut(32)
                        .build())
                // Block 2
                .layer(new ConvolutionLayer.Builder()
                        .name("cnn2")
                        .cudnnAlgoMode(ConvolutionLayer.AlgoMode.PREFER_FASTEST)
                        .kernelSize(2, 2)
                        .stride(1, 1)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.RELU)
                        .nOut(64)
                        .build())
                // Block 3
                .layer(new ConvolutionLayer.Builder()
                        .name("cnn3")
                        .cudnnAlgoMode(ConvolutionLayer.AlgoMode.PREFER_FASTEST)
                        .kernelSize(2, 2)
                        .stride(1, 1)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.RELU)
                        .nOut(128)
                        .build())
                // Block 4
                .layer(new ConvolutionLayer.Builder()
                        .name("cnn4")
                        .cudnnAlgoMode(ConvolutionLayer.AlgoMode.PREFER_FASTEST)
                        .kernelSize(2, 2)
                        .stride(1, 1)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.RELU)
                        .nOut(256)
                        .build())
                // Block 5
                .layer(new ConvolutionLayer.Builder()
                        .name("cnn5")
                        .cudnnAlgoMode(ConvolutionLayer.AlgoMode.PREFER_FASTEST)
                        .kernelSize(2, 2)
                        .stride(1, 1)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.RELU)
                        .nOut(256)
                        .build())
                // Fully connected layer
                .layer(new DenseLayer.Builder()
                        .name("ffn1")
                        .activation(Activation.RELU)
                        .nOut(256)
                        .build())
                // Output layer
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(CLASSES_COUNT)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutionalFlat(this.upperBound,this.upperBound,nChannels))
                .build();

        log.info("Training models...");

        // Train the x-coordinate CNN model
        this.xCoordClassifierNetwork = new MultiLayerNetwork(configuration);
        this.xCoordClassifierNetwork.init();
        for (int l = 0; l < numOfEpochs; l++) {
            this.xCoordClassifierNetwork.fit(xTrainingData);
        }

        // Train the y-coordinate CNN model
        this.yCoordClassifierNetwork = new MultiLayerNetwork(configuration);
        this.yCoordClassifierNetwork.init();
        for (int l = 0; l < numOfEpochs; l++) {
            this.yCoordClassifierNetwork.fit(yTrainingData);
        }

        // Save models to files
        File xModelLocation = new File("trained_x_model.zip");

        try {
            ModelSerializer.writeModel(this.xCoordClassifierNetwork, xModelLocation, this.saveUpdater);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File yModelLocation = new File("trained_y_model.zip");

        try {
            ModelSerializer.writeModel(this.yCoordClassifierNetwork, yModelLocation, this.saveUpdater);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Main method to be called by the Android UI app's side that returns the PointF object to be
    // drawn on the floor plan map.
    // Input format: {image representation of Wi-Fi AP BSSID-RSSI list} as an INDArray.
    // Output format: [{percentage of image width, confidence rate}, {percentage of
    // image height, confidence rate}] as an INDArray.
    // Divide the image into 100 classes or bins for each axis of the coordinates to get the
    // "percentage" values.
    // Do take note that the model might be less accurate on Level 1 compared to Level 2 due to
    // the larger height (Wi-Fi APs being further away would lead to less difference in RSSI
    // values due to inverse-square law), as well as due to the fact that prediction on Level 2
    // can rely on APs both above and below the user, whereas prediction on Level 1 can only rely
    // on APs above the user.
    public Pair<Float, Float> predict(INDArray input) {
        return null;
    }
}
