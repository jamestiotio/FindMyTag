package com.example.findmytag.algorithms.neuralnetwork;

import java.util.Map;

/**
 * This base coordinate system is used for both mapping and testing (target is WiFi AP for
 * mapping and user for testing).
 * Identifier of each access point could be its BSSID/MAC address or its local IP address (since
 * there are multiple access points with the same SSID name).
 * Do take note that one access point could potentially emit multiple SSIDs as well (perhaps on
 * different channels).
 */
public class BaseCoordinateSystem {
    private static final Coordinate refPosition = new Coordinate(0, 0, 0);
    private Coordinate targetPosition;
    private Map<Coordinate, Integer> PositionList;

    public BaseCoordinateSystem(double targetX, double targetY, double targetZ, Map<Coordinate,
            Integer> inputPositionList) {
        this.targetPosition = new Coordinate(targetX, targetY, targetZ);
        for (Map.Entry<Coordinate, Integer> entry : inputPositionList.entrySet()) {
            this.PositionList.put(entry.getKey(), entry.getValue());
        }
    }
}