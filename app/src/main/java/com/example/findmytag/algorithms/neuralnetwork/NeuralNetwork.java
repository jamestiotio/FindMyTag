// package com.example.findmytag.algorithms.neuralnetwork;

/**
 * This is just a simple feed-forward neural network, nothing fancy. This network uses RSSI instead
 * of CSI information data. Based on this research paper:
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

}
