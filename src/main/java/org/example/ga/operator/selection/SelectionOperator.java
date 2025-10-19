package org.example.ga.operator.selection;

import org.example.ga.Chromosome;
import org.example.ga.Population;

public interface SelectionOperator {
    Chromosome select(Population population);
}