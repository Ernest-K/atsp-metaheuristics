package org.example.ga.operator.mutation;

import org.example.ga.Chromosome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SwapMutation implements MutationOperator {

    private final Random random = new Random();

    @Override
    public Chromosome mutate(Chromosome chromosome) {
        List<Integer> mutatedTour = new ArrayList<>(chromosome.getTour());
        int size = mutatedTour.size();

        int pos1 = random.nextInt(size);
        int pos2 = random.nextInt(size);
        while (pos1 == pos2) {
            pos2 = random.nextInt(size);
        }

        Collections.swap(mutatedTour, pos1, pos2);

        return new Chromosome(mutatedTour);
    }
}