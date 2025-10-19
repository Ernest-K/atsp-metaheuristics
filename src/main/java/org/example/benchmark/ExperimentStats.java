package org.example.benchmark;

import org.example.problem.TSPSolution;

import java.util.IntSummaryStatistics;
import java.util.List;

public class ExperimentStats {
    private final String experimentName;
    private final int bestDistance;
    private final int worstDistance;
    private final double averageDistance;
    private final double stdDeviationDistance;
    private final double averageTimeMillis;
    private final double averageGapPercent;
    private final double bestGapPercent;

    ExperimentStats(String name, int best, int worst, double avg, double stdDev, double avgTime, double bestGap, double avgGap) {
        this.experimentName = name;
        this.bestDistance = best;
        this.worstDistance = worst;
        this.averageDistance = avg;
        this.stdDeviationDistance = stdDev;
        this.averageTimeMillis = avgTime;
        this.bestGapPercent = bestGap;
        this.averageGapPercent = avgGap;
    }

    public static ExperimentStats calculate(String name, List<TSPSolution> results, int optimalDistance) {
        IntSummaryStatistics stats = results.stream()
                .mapToInt(TSPSolution::getDistance)
                .summaryStatistics();

        double avgTime = results.stream()
                .mapToLong(TSPSolution::getExecutionTimeMillis)
                .average()
                .orElse(0.0);

        double avgDistance = stats.getAverage();

        double stdDev = Math.sqrt(results.stream()
                .mapToDouble(TSPSolution::getDistance)
                .map(d -> Math.pow(d - avgDistance, 2))
                .sum() / results.size());

        double bestGap = ((double) stats.getMin() - optimalDistance) / optimalDistance * 100.0;
        double avgGap = (avgDistance - optimalDistance) / optimalDistance * 100.0;

        return new ExperimentStats(name, stats.getMin(), stats.getMax(), avgDistance, stdDev, avgTime, bestGap, avgGap);
    }

    public String getExperimentName() { return experimentName; }
    public int getBestDistance() { return bestDistance; }
    public int getWorstDistance() { return worstDistance; }
    public double getAverageDistance() { return averageDistance; }
    public double getStdDeviationDistance() { return stdDeviationDistance; }
    public double getAverageTimeMillis() { return averageTimeMillis; }
    public double getAverageGapPercent() { return averageGapPercent; }
    public double getBestGapPercent() { return bestGapPercent; }
}
