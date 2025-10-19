package org.example.ga.operator.mutation;

import org.example.ga.Chromosome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InversionMutation implements MutationOperator {

    private final Random random = new Random();

    @Override
    public Chromosome mutate(Chromosome chromosome) {
        List<Integer> tour = new ArrayList<>(chromosome.getTour());
        int size = tour.size();

        int start = random.nextInt(size);
        int end = random.nextInt(size);
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        List<Integer> subList = tour.subList(start, end + 1);
        Collections.reverse(subList);

        return new Chromosome(tour);
    }
}
