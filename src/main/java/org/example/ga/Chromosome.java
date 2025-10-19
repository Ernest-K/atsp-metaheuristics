package org.example.ga;

import org.example.problem.TSPInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reprezentuje pojedynczego osobnika w populacji, czyli jedną możliwą trasę (rozwiązanie).
 * Chromosom jest zdefiniowany jako permutacja miast.
 */
public class Chromosome {

    private final List<Integer> tour;

    private double fitness = -1.0;

    private int distance = -1;

    /**
     * Konstruktor kopiujący. Tworzy nowy chromosom na podstawie istniejącej trasy.
     * @param tour lista miast reprezentująca trasę.
     */
    public Chromosome(List<Integer> tour) {
        this.tour = new ArrayList<>(tour);
    }

    /**
     * Konstruktor tworzący losowy chromosom (losową trasę).
     * @param dimension liczba miast do uwzględnienia w trasie.
     */
    public Chromosome(int dimension) {
        this.tour = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            this.tour.add(i);
        }
        Collections.shuffle(this.tour);
    }

    /**
     * Oblicza całkowitą długość trasy reprezentowanej przez ten chromosom.
     * Wynik jest zapamiętywany w polu 'distance'.
     * @param instance instancja problemu TSP zawierająca macierz odległości.
     * @return całkowita długość trasy.
     */
    public int calculateDistance(TSPInstance instance) {
        if (distance == -1) {
            int tourDistance = 0;
            for (int i = 0; i < tour.size(); i++) {
                int fromCity = tour.get(i);
                int toCity = (i + 1 < tour.size()) ? tour.get(i + 1) : tour.get(0);
                tourDistance += instance.getDistance(fromCity, toCity);
            }
            this.distance = tourDistance;
        }
        return this.distance;
    }

    /**
     * Oblicza wartość funkcji przystosowania.
     * Dla problemu minimalizacji, fitness jest odwrotnością dystansu.
     * @param instance instancja problemu, potrzebna do obliczenia dystansu.
     * @return wartość fitness.
     */
    public double getFitness(TSPInstance instance) {
        if (fitness == -1.0) {
            fitness = 1.0 / calculateDistance(instance);
        }
        return fitness;
    }

    public List<Integer> getTour() {
        return Collections.unmodifiableList(tour);
    }

    public int getDistance() {
        if (distance == -1) {
            throw new IllegalStateException("Dystans nie został jeszcze obliczony. Wywołaj najpierw calculateDistance().");
        }
        return distance;
    }

    @Override
    public String toString() {
        return "Trasa: " + tour + " | Dystans: " + (distance == -1 ? "N/A" : distance);
    }
}
