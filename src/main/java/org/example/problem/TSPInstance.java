package org.example.problem;

public class TSPInstance {
    private final int[][] distanceMatrix;
    private final int dimension;
    private final int optimalDistance;

    public TSPInstance(int[][] distanceMatrix, int optimalDistance) {
        this.distanceMatrix = distanceMatrix;
        this.dimension = distanceMatrix.length;
        this.optimalDistance = optimalDistance;
    }

    public int getDistance(int fromCity, int toCity) {
        return distanceMatrix[fromCity][toCity];
    }

    public int getDimension() {
        return dimension;
    }

    public int getOptimalDistance() {
        return optimalDistance;
    }
}
