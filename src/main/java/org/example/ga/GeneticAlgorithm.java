package org.example.ga;

import org.example.ga.operator.crossover.CrossoverOperator;
import org.example.ga.operator.mutation.MutationOperator;
import org.example.ga.operator.selection.SelectionOperator;
import org.example.problem.TSPInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneticAlgorithm {

    private final TSPInstance problemInstance;
    private final GaConfig config;
    private final SelectionOperator selectionOperator;
    private final CrossoverOperator crossoverOperator;
    private final MutationOperator mutationOperator;
    private final Random random = new Random();

    private Chromosome bestSolutionSoFar;

    public GeneticAlgorithm(TSPInstance problemInstance, GaConfig config, SelectionOperator selectionOperator,
                            CrossoverOperator crossoverOperator, MutationOperator mutationOperator) {
        this.problemInstance = problemInstance;
        this.config = config;
        this.selectionOperator = selectionOperator;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
    }

    /**
     * Uruchamia proces ewolucji.
     * @return Najlepszy znaleziony chromosom (trasa) po zakończeniu wszystkich generacji.
     */
    public Chromosome run() {
        Population population = new Population(config.getPopulationSize(), problemInstance);
        population.calculateFitness(problemInstance);
        bestSolutionSoFar = population.getFittest();

        for (int generation = 1; generation <= config.getGenerations(); generation++) {
            List<Chromosome> newChromosomes = new ArrayList<>();

            List<Chromosome> elite = population.getChromosomes().stream()
                    .sorted(Comparator.comparingInt(Chromosome::getDistance))
                    .limit(config.getElitismCount())
                    .toList();
            newChromosomes.addAll(elite);

            while (newChromosomes.size() < config.getPopulationSize()) {
                Chromosome parent1 = selectionOperator.select(population);
                Chromosome parent2 = selectionOperator.select(population);

                Chromosome child;
                if (random.nextDouble() < config.getCrossoverRate()) {
                    child = crossoverOperator.crossover(parent1, parent2);
                } else {
                    child = new Chromosome(parent1.getTour());
                }

                if (random.nextDouble() < config.getMutationRate()) {
                    child = mutationOperator.mutate(child);
                }

                newChromosomes.add(child);
            }

            population = new Population(newChromosomes);
            population.calculateFitness(problemInstance);

            Chromosome currentBest = population.getFittest();
            if (currentBest.getDistance() < bestSolutionSoFar.getDistance()) {
                bestSolutionSoFar = currentBest;
            }
        }

        return bestSolutionSoFar;
    }
}
