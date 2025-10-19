package org.example.ga;

public final class GaConfig {

    private final int populationSize;
    private final int generations;
    private final double crossoverRate;
    private final double mutationRate;
    private final int tournamentSize;
    private final int elitismCount;

    public GaConfig(int populationSize, int generations, double crossoverRate, double mutationRate, int tournamentSize, int elitismCount) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.tournamentSize = tournamentSize;
        this.elitismCount = elitismCount;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getGenerations() {
        return generations;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public int getElitismCount() {
        return elitismCount;
    }
}