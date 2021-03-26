// package com.example.findmytag.algorithms.neuralnetwork;

import java.util.Arrays;
import java.util.List;

/**
 * This is just a simple feed-forward convolutional neural network (Conv-ReLu CNN-LOC), nothing
 * fancy. This network uses RSSI instead of CSI information data. Based on this research paper:
 * https://ieeexplore.ieee.org/document/8662548, using CSI data will result in relatively better and
 * more accurate localization results. However, no current smartphone API allows access to the Wi-Fi
 * AP's physical layer CSI information (this includes the Android API). Some level of hacking to the
 * lower layers could be executed but its legality is questionable. Manual offline mapping using the
 * Linux 802.11n CSI Tool (https://dhalperi.github.io/linux-80211n-csitool/) could be conducted but
 * this is not entirely in line with the project's requirements to actually conduct the mapping
 * on-the-fly on the smartphone mobile device itself.
 * 
 * Regarding scalability issues, this network is scalable in terms of different buildings (with
 * different physical measurements such as the attenuation and propagation constants) and different
 * numbers/arrangements of Wi-Fi access points with different specifications/models, but this may
 * not generally scale to N floors (this is only for 2 floors).
 */
public class NeuralNetwork {
    // Use these to only selectively identify and take into account the desired AP signals (instead
    // of other random BSSIDs that might come by and visit the proximity)
    private final List<String> KNOWN_B2L1_AP_LIST = Arrays.asList("18-64-72-CD-5F-EC",
            "18-64-72-CD-5F-E6", "18-64-72-CD-60-00", "18-64-72-CD-5F-BE", "18-64-72-CD-5F-C6",
            "18-64-72-CD-60-88", "18-64-72-CD-60-9A", "18-64-72-CD-5F-B8", "18-64-72-CD-5F-38",
            "18-64-72-CD-5F-F4", "18-64-72-CD-60-14", "18-64-72-CD-60-DE", "18-64-72-CD-60-4A",
            "18-64-72-CD-60-D6", "18-64-72-CD-60-56", "18-64-72-CD-60-36", "18-64-72-CD-65-E6",
            "18-64-72-CD-5F-8C", "18-64-72-CD-60-7A");
    private final List<String> KNOWN_B2L2_AP_LIST = Arrays.asList("18-64-72-CD-5F-06",
            "18-64-72-CD-60-AE", "18-64-72-CD-5F-C8", "18-64-72-CD-62-E0", "18-64-72-CD-60-8A",
            "18-64-72-CD-62-3C", "18-64-72-CD-62-40", "18-64-72-CD-62-74", "18-64-72-CD-5F-0E",
            "18-64-72-CD-53-48", "18-64-72-CD-64-34", "18-64-72-CD-65-C4", "18-64-72-CD-64-4C",
            "18-64-72-CD-62-54", "18-64-72-CD-62-78", "18-64-72-CD-62-80", "18-64-72-CD-62-82",
            "18-64-72-CD-61-38", "18-64-72-CD-53-A4", "18-64-72-CD-66-7C", "18-64-72-CD-52-AC",
            "18-64-72-CD-53-70", "18-64-72-CD-52-BC", "18-64-72-CD-64-AA", "18-64-72-CD-65-A6",
            "18-64-72-CD-66-8C", "18-64-72-CD-5F-54");
}
