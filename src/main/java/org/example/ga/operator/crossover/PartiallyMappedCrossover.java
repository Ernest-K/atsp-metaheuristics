package org.example.ga.operator.crossover;

import org.example.ga.Chromosome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PartiallyMappedCrossover implements CrossoverOperator {

    private final Random random = new Random();

    @Override
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        int size = parent1.getTour().size();
        List<Integer> p1 = parent1.getTour();
        List<Integer> p2 = parent2.getTour();

        List<Integer> childTour = new ArrayList<>(Collections.nCopies(size, -1));

        int start = random.nextInt(size);
        int end = random.nextInt(size);
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        for (int i = start; i <= end; i++) {
            childTour.set(i, p1.get(i));
        }

        for (int i = start; i <= end; i++) {
            int cityFromP2 = p2.get(i);
            if (!childTour.contains(cityFromP2)) {
                int currentPos = i;
                int cityToPlace = p1.get(currentPos);

                while (childTour.get(p2.indexOf(cityToPlace)) != -1) {
                    currentPos = p2.indexOf(cityToPlace);
                    cityToPlace = p1.get(currentPos);
                }
                childTour.set(p2.indexOf(cityToPlace), cityFromP2);
            }
        }

        for (int i = 0; i < size; i++) {
            if (childTour.get(i) == -1) {
                childTour.set(i, p2.get(i));
            }
        }

        return new Chromosome(childTour);
    }
}