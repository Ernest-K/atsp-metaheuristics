package org.example.ga.operator.selection;

import org.example.ga.Chromosome;
import org.example.ga.Population;

import java.util.List;
import java.util.Random;

/**
 * Implementacja selekcji kołem ruletki.
 * Prawdopodobieństwo wyboru osobnika jest proporcjonalne do jego wartości fitness.
 */
public class RouletteWheelSelection implements SelectionOperator {

    private final Random random = new Random();

    @Override
    public Chromosome select(Population population) {
        List<Chromosome> chromosomes = population.getChromosomes();

        double totalFitness = chromosomes.stream()
                .mapToDouble(c -> 1.0 / c.getDistance())
                .sum();

        double randomPick = random.nextDouble() * totalFitness;

        double currentSum = 0;
        for (Chromosome chromosome : chromosomes) {
            currentSum += (1.0 / chromosome.getDistance());
            if (currentSum >= randomPick) {
                return chromosome;
            }
        }

        return chromosomes.get(chromosomes.size() - 1);
    }
}