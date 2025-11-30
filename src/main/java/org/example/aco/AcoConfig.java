package org.example.aco;

public final class AcoConfig {

    private final int antCount; // Liczba mrówek w kolonii
    private final int iterations; // Liczba cykli (pokoleń)
    private final double alpha; // Wpływ feromonów na decyzję
    private final double beta; // Wpływ heurystyki (dystansu) na decyzję
    private final double evaporationRate; // Współczynnik parowania feromonów (rho)
    private final double initialPheromone; // Początkowa wartość feromonu na ścieżkach

    public AcoConfig(int antCount, int iterations, double alpha, double beta, double evaporationRate, double initialPheromone) {
        this.antCount = antCount;
        this.iterations = iterations;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.initialPheromone = initialPheromone;
    }

    public int getAntCount() { return antCount; }
    public int getIterations() { return iterations; }
    public double getAlpha() { return alpha; }
    public double getBeta() { return beta; }
    public double getEvaporationRate() { return evaporationRate; }
    public double getInitialPheromone() { return initialPheromone; }
}
