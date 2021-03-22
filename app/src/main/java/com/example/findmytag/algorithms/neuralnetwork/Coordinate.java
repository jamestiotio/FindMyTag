import java.lang.Math;

/**
 * Assumes that the vertical height/altitude difference between any routers and the user is the same (constant) throughout the building.
 * Distances are measured in units of metres.
 */
public class Coordinate {
    private static final double USER_ALTITUDE = 0;
    private static final double ROUTER_ALTITUDE = 1;
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Get three-dimensional Euclidean distance
    public double getDistance(Coordinate c) {
        return Math.sqrt(Math.pow(Math.abs(c.getX() - this.getX()), 2) + Math.pow(Math.abs(c.getY() - this.getY()), 2) + Math.pow(Math.abs(ROUTER_ALTITUDE - USER_ALTITUDE), 2));
    }
}