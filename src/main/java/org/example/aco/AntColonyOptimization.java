package org.example.aco;

import org.example.problem.TSPInstance;
import org.example.problem.TSPSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColonyOptimization {
    private final TSPInstance problemInstance;
    private final AcoConfig config;
    private final int numCities;
    private final double[][] pheromones; // Macierz feromonów
    private final double[][] heuristicInfo; // Macierz informacji heurystycznej (1/dystans)
    private TSPSolution bestSolutionSoFar;
    private final Random random = new Random();

    public AntColonyOptimization(TSPInstance problemInstance, AcoConfig config) {
        this.problemInstance = problemInstance;
        this.config = config;
        this.numCities = problemInstance.getDimension();
        this.pheromones = new double[numCities][numCities];
        this.heuristicInfo = new double[numCities][numCities];
        initialize();
    }

    private void initialize() {
        // Inicjalizuj macierz feromonów
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] = config.getInitialPheromone();
            }
        }
        // Oblicz i zapisz macierz heurystyczną (aby nie liczyć 1/d w pętli)
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i != j) {
                    heuristicInfo[i][j] = 1.0 / problemInstance.getDistance(i, j);
                }
            }
        }
    }

    public TSPSolution run() {
        long startTime = System.currentTimeMillis();

        for (int iter = 0; iter < config.getIterations(); iter++) {
            List<Ant> ants = createAnts();

            // Każda mrówka buduje swoją trasę
            for (Ant ant : ants) {
                ant.buildTour(pheromones, heuristicInfo, config.getAlpha(), config.getBeta());
            }

            // Znajdź najlepsze rozwiązanie w tej iteracji i zaktualizuj globalne najlepsze
            updateBestSolution(ants);

            // Zaktualizuj feromony: najpierw parowanie, potem nanoszenie
            evaporatePheromones();
            applyPheromoneDeposit(ants);

//            if ((iter + 1) % 50 == 0) {
//                System.out.printf("Iteracja %d: Najlepszy dystans = %d%n", iter + 1, bestSolutionSoFar.getDistance());
//            }
        }

        long endTime = System.currentTimeMillis();

        // Zwróć finalny, najlepszy wynik w standardowym formacie
        return new TSPSolution(
                bestSolutionSoFar.getTour(),
                bestSolutionSoFar.getDistance(),
                endTime - startTime
        );
    }

    private List<Ant> createAnts() {
        List<Ant> ants = new ArrayList<>();
        for (int i = 0; i < config.getAntCount(); i++) {
            // Każda mrówka startuje z losowego miasta
            ants.add(new Ant(numCities, random.nextInt(numCities), problemInstance));
        }
        return ants;
    }

    private void evaporatePheromones() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] *= (1.0 - config.getEvaporationRate());
            }
        }
    }

    private void applyPheromoneDeposit(List<Ant> ants) {
        for (Ant ant : ants) {
            List<Integer> tour = ant.getTour();
            int distance = ant.getTourDistance();
            double depositAmount = 1.0 / distance; // Im krótsza trasa, tym więcej feromonów

            for (int i = 0; i < numCities; i++) {
                int from = tour.get(i);
                int to = tour.get((i + 1) % numCities);
                pheromones[from][to] += depositAmount;
                pheromones[to][from] += depositAmount; // W ATSP można rozważyć asymetryczne nanoszenie
            }
        }
    }

    private void updateBestSolution(List<Ant> ants) {
        for (Ant ant : ants) {
            if (bestSolutionSoFar == null || ant.getTourDistance() < bestSolutionSoFar.getDistance()) {
                bestSolutionSoFar = new TSPSolution(
                        ant.getTour(),
                        ant.getTourDistance(),
                        0 // Czas nie jest tu istotny
                );
            }
        }
    }
}
