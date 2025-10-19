package org.example.sa;

import org.example.problem.TSPInstance;
import org.example.sa.operator.NeighborStrategy;

import java.util.Random;

public class SimulatedAnnealing {

    private final TSPInstance problemInstance;
    private final SaConfig config;
    private final NeighborStrategy neighborGenerator;
    private final Random random = new Random();

    public SimulatedAnnealing(TSPInstance problemInstance, SaConfig config, NeighborStrategy neighborGenerator) {
        this.problemInstance = problemInstance;
        this.config = config;
        this.neighborGenerator = neighborGenerator;
    }

    /**
     * Funkcja prawdopodobieństwa akceptacji gorszego rozwiązania.
     * @param energyDelta Różnica w jakości (dystansie). Zawsze dodatnia.
     * @param temperature Aktualna temperatura.
     * @return Prawdopodobieństwo w zakresie [0, 1].
     */
    private double acceptanceProbability(int energyDelta, double temperature) {
        return Math.exp(-energyDelta / temperature);
    }

    /**
     * Uruchamia algorytm symulowanego wyżarzania.
     * @return Najlepsza znaleziona trasa.
     */
    public SaSolution  run() {
        SaSolution currentSolution = new SaSolution(problemInstance.getDimension());
        currentSolution.calculateDistance(problemInstance);

        SaSolution bestSolution = new SaSolution(currentSolution.getTour());
        bestSolution.calculateDistance(problemInstance);

        System.out.printf("SA: Początkowy dystans = %d%n", currentSolution.getDistance());

        double currentTemperature = config.getInitialTemperature();

        while (currentTemperature > config.getMinimumTemperature()) {
            for (int i = 0; i < config.getIterationsPerTemperature(); i++) {
                SaSolution newSolution = neighborGenerator.generate(currentSolution);
                newSolution.calculateDistance(problemInstance);

                int currentEnergy = currentSolution.getDistance();
                int newEnergy = newSolution.getDistance();
                int energyDelta = newEnergy - currentEnergy;

                if (energyDelta < 0 || acceptanceProbability(energyDelta, currentTemperature) > random.nextDouble()) {
                    currentSolution = newSolution;
                }

                if (currentSolution.getDistance() < bestSolution.getDistance()) {
                    bestSolution = currentSolution;
                }
            }
            currentTemperature *= config.getCoolingRate();
        }

        return bestSolution;
    }
}
