package org.example.ga.operator.crossover;

import org.example.ga.Chromosome;

import java.util.*;
import java.util.stream.Collectors;

public class OrderedCrossover implements CrossoverOperator {

    private final Random random = new Random();

    @Override
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        int size = parent1.getTour().size();
        List<Integer> parent1Tour = parent1.getTour();
        List<Integer> parent2Tour = parent2.getTour();

        int start = random.nextInt(size);
        int end = random.nextInt(size);
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        List<Integer> childTour = new ArrayList<>(Collections.nCopies(size, null));
        Set<Integer> copiedCities = new HashSet<>();
        for (int i = start; i <= end; i++) {
            int city = parent1Tour.get(i);
            childTour.set(i, city);
            copiedCities.add(city);
        }

        List<Integer> remainingCities = parent2Tour.stream()
                .filter(city -> !copiedCities.contains(city))
                .collect(Collectors.toList());

        int cityIndex = 0;
        for (int i = 0; i < size; i++) {
            int currentIndex = (end + 1 + i) % size;
            if (childTour.get(currentIndex) == null) {
                childTour.set(currentIndex, remainingCities.get(cityIndex++));
            }
        }

        return new Chromosome(childTour);
    }
}