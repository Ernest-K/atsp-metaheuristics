package org.example.ga.operator.mutation;

import org.example.ga.Chromosome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ScrambleMutation implements MutationOperator {

    private final Random random = new Random();

    @Override
    public Chromosome mutate(Chromosome chromosome) {
        List<Integer> tourCities = new ArrayList<>(chromosome.getTour());
        int size = tourCities.size();

        if (size < 3) {
            return new Chromosome(chromosome.getTour());
        }

        int start = random.nextInt(size);
        int end = random.nextInt(size);
        while (start == end) {
            end = random.nextInt(size);
        }

        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        List<Integer> sublistToScramble = tourCities.subList(start, end + 1);

        Collections.shuffle(sublistToScramble);

        return new Chromosome(tourCities);
    }
}
