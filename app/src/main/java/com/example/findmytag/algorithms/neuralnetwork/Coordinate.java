package com.example.findmytag.algorithms.neuralnetwork;

/**
 * Assumes that the vertical height/altitude difference between any routers and the user is the
 * same (constant) throughout the building.
 * Distances are measured in units of metres.
 */
public class Coordinate {
    private double x;
    private double y;
    private double z;   // Level

    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    // Get three-dimensional Euclidean distance
    public double getDistance(Coordinate c) {
        return Math.sqrt(Math.pow(Math.abs(c.getX() - this.getX()), 2) + Math.pow(Math.abs(c.getY() - this.getY()), 2) + Math.pow(Math.abs(c.getZ() - this.getZ()), 2));
    }
}