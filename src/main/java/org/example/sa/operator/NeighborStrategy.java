package org.example.sa.operator;

import org.example.sa.SaSolution;

@FunctionalInterface
public interface NeighborStrategy {
    SaSolution generate(SaSolution solution);
}