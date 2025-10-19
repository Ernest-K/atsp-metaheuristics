package org.example.sa.operator;

import org.example.sa.SaSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SwapNeighbor implements NeighborStrategy {
    private final Random random = new Random();

    @Override
    public SaSolution generate(SaSolution solution) {
        List<Integer> newTour = new ArrayList<>(solution.getTour());
        int size = newTour.size();

        int pos1 = random.nextInt(size);
        int pos2 = random.nextInt(size);
        while (pos1 == pos2) {
            pos2 = random.nextInt(size);
        }

        Collections.swap(newTour, pos1, pos2);
        return new SaSolution(newTour);
    }
}