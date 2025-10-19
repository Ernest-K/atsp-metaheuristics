package org.example.sa;

import org.example.problem.TSPInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaSolution {
    private final List<Integer> tour;
    private int distance = -1;

    public SaSolution(List<Integer> tour) {
        this.tour = new ArrayList<>(tour);
    }

    public SaSolution(int dimension) {
        this.tour = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            this.tour.add(i);
        }
        Collections.shuffle(this.tour);
    }

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

    public List<Integer> getTour() {
        return Collections.unmodifiableList(tour);
    }

    public int getDistance() {
        if (distance == -1) {
            throw new IllegalStateException("Dystans nie został jeszcze obliczony. Wywołaj najpierw calculateDistance().");
        }
        return distance;
    }
}
