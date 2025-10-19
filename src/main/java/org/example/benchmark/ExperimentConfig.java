package org.example.benchmark;

import org.example.ga.GaConfig;
import org.example.ga.operator.crossover.CrossoverOperator;
import org.example.ga.operator.mutation.MutationOperator;
import org.example.ga.operator.selection.SelectionOperator;

public class ExperimentConfig {
    private final String name;
    private final GaConfig gaConfig;
    private final SelectionOperator selectionOperator;
    private final CrossoverOperator crossoverOperator;
    private final MutationOperator mutationOperator;

    public ExperimentConfig(String name, GaConfig gaConfig, SelectionOperator selection,
                            CrossoverOperator crossover, MutationOperator mutation) {
        this.name = name;
        this.gaConfig = gaConfig;
        this.selectionOperator = selection;
        this.crossoverOperator = crossover;
        this.mutationOperator = mutation;
    }

    public String getName() { return name; }
    public GaConfig getGaConfig() { return gaConfig; }
    public SelectionOperator getSelectionOperator() { return selectionOperator; }
    public CrossoverOperator getCrossoverOperator() { return crossoverOperator; }
    public MutationOperator getMutationOperator() { return mutationOperator; }
}
