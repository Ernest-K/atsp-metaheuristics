package org.example.aco;

import org.example.problem.TSPInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

class Ant {
    private final int numCities;
    private final List<Integer> tour;
    private final Set<Integer> unvisitedCities;
    private final TSPInstance problemInstance;
    private int tourDistance = -1;
    private final Random random = new Random();

    public Ant(int numCities, int startCity, TSPInstance problemInstance) {
        this.numCities = numCities;
        this.problemInstance = problemInstance;
        this.tour = new ArrayList<>(numCities);
        this.unvisitedCities = new HashSet<>(numCities);

        for (int i = 0; i < numCities; i++) {
            if (i != startCity) {
                unvisitedCities.add(i);
            }
        }
        tour.add(startCity);
    }

    public void buildTour(double[][] pheromones, double[][] heuristicInfo, double alpha, double beta) {
        while (!unvisitedCities.isEmpty()) {
            int currentCity = tour.get(tour.size() - 1);
            int nextCity = selectNextCity(currentCity, pheromones, heuristicInfo, alpha, beta);
            tour.add(nextCity);
            unvisitedCities.remove(nextCity);
        }
        calculateTourDistance();
    }

    private int selectNextCity(int currentCity, double[][] pheromones, double[][] heuristicInfo, double alpha, double beta) {
        double[] probabilities = new double[numCities];
        double probabilitiesSum = 0.0;

        // Oblicz "atrakcyjność" każdej nieodwiedzonej ścieżki
        for (int city : unvisitedCities) {
            double pheromone = Math.pow(pheromones[currentCity][city], alpha);
            double heuristic = Math.pow(heuristicInfo[currentCity][city], beta);
            probabilities[city] = pheromone * heuristic;
            probabilitiesSum += probabilities[city];
        }

        // Wybierz następne miasto za pomocą mechanizmu koła ruletki
        double randomPick = random.nextDouble() * probabilitiesSum;
        double currentSum = 0;
        for (int city : unvisitedCities) {
            currentSum += probabilities[city];
            if (currentSum >= randomPick) {
                return city;
            }
        }

        // W rzadkich przypadkach (błędy numeryczne) zwróć dowolne nieodwiedzone
        return unvisitedCities.iterator().next();
    }

    private void calculateTourDistance() {
        int distance = 0;
        for (int i = 0; i < numCities; i++) {
            int from = tour.get(i);
            int to = tour.get((i + 1) % numCities);
            distance += problemInstance.getDistance(from, to);
        }
        this.tourDistance = distance;
    }

    public List<Integer> getTour() { return tour; }
    public int getTourDistance() { return tourDistance; }
}