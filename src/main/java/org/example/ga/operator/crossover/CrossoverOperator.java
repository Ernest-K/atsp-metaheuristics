package org.example.ga.operator.crossover;

import org.example.ga.Chromosome;


public interface CrossoverOperator {
    Chromosome crossover(Chromosome parent1, Chromosome parent2);
}