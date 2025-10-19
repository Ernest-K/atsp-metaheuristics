package org.example.ga.operator.mutation;

import org.example.ga.Chromosome;

public interface MutationOperator {
    Chromosome mutate(Chromosome chromosome);
}