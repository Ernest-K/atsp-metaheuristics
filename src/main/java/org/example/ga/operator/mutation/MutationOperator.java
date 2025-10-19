package org.example.ga.operator.mutation;

import org.example.ga.Chromosome;

public interface MutationOperator {
    // Mutacja zazwyczaj modyfikuje chromosom, a nie tworzy nowy
    Chromosome mutate(Chromosome chromosome);
}