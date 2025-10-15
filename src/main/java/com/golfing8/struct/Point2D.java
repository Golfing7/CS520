package com.golfing8.struct;

import java.util.Comparator;

public record Point2D(double x, double y) {
    public static final Comparator<Point2D> X = Comparator.comparingDouble(p -> p.x);
    public static final Comparator<Point2D> Y = Comparator.comparingDouble(p -> p.y);

    /**
     * Finds the euclidean distance from this point to the other
     *
     * @param other the other point
     * @return the distance
     */
    public double distance(Point2D other) {
        double distX = this.x - other.x;
        double distY = this.y - other.y;
        return Math.sqrt(distX * distX + distY * distY);
    }
}
