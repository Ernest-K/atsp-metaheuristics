package org.example.problem;

import java.util.List;

public final class TSPSolution {

    private final List<Integer> tour;
    private final int distance;
    private final long executionTimeMillis;

    public TSPSolution(List<Integer> tour, int distance, long executionTimeMillis) {
        this.tour = tour;
        this.distance = distance;
        this.executionTimeMillis = executionTimeMillis;
    }

    // Gettery
    public List<Integer> getTour() {
        return tour;
    }

    public int getDistance() {
        return distance;
    }

    public long getExecutionTimeMillis() {
        return executionTimeMillis;
    }
}
